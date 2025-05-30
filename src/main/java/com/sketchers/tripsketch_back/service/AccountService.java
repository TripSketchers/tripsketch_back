package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.Mypage.ShareTripReqDto;
import com.sketchers.tripsketch_back.dto.Mypage.ShareTripRespDto;
import com.sketchers.tripsketch_back.dto.Mypage.TripShareDto;
import com.sketchers.tripsketch_back.dto.PasswordChangeReqDto;
import com.sketchers.tripsketch_back.dto.TripRespDto;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.entity.TripShare;
import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.exception.AuthMailException;
import com.sketchers.tripsketch_back.exception.SendMailException;
import com.sketchers.tripsketch_back.exception.TripNotFoundException;
import com.sketchers.tripsketch_back.exception.UnauthorizedAccessException;
import com.sketchers.tripsketch_back.jwt.JwtProvider;
import com.sketchers.tripsketch_back.repository.AccountMapper;
import com.sketchers.tripsketch_back.repository.AuthMapper;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final JavaMailSender javaMailSender;
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${server.serverAddress}")
    private String serverAddress;

    public int deleteUser(int userId) {
        return accountMapper.deleteUser(userId);
    }

    public boolean sendAuthMail() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String toEmail = principalUser.getUser().getEmail();

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "utf-8");
            helper.setSubject("TripSketch 이메일 인증");
            helper.setFrom("mini011029@gmail.com");
            helper.setTo(toEmail);

            String token = jwtProvider.generateAuthMailToken(toEmail);      //이메일 인증을 위한 토큰 발행

            // 토큰 삽입한 HTML
            mimeMailMessage.setText(
                "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9;\">" +
                    "<div style=\"max-width: 500px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">" +
                        "<h1 style=\"color: #3b5c80;\">TripSketch 이메일 인증</h1>" +
                        "<p>안녕하세요! TripSketch를 이용해 주셔서 감사합니다.</p>" +
                        "<p>이메일 인증을 완료하려면 아래 버튼을 클릭해주세요.</p>" +
                        "<div style=\"text-align: center; margin: 30px 0;\">" +
                            "<a href='http://" + ("localhost".equals(serverAddress) ? "localhost:8080" : serverAddress) + "/api/account/auth/email?token=" + token + "'" +
                            " style='display: inline-block; background-color: #3b5c80; color: #ffffff; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-size: 16px;'>" +
                            "이메일 인증하기</a>" +
                        "</div>" +
                        "<p style='font-size: 14px; color: #666;'>본 메일은 발신 전용이며, 인증 유효 시간은 5분입니다.</p>" +
                    "</div>" +
                "</div>", "utf-8", "html");

            javaMailSender.send(mimeMailMessage);       //설정한 메시지를 sender를 통해 전달함
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendMailException("이메일 전송이 실패했습니다.");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean authenticateMail(String token) {
        Claims claims = jwtProvider.getClaims(token);
        if(claims == null) {    // 유효하지 않은 토큰
            throw new AuthMailException("만료된 인증 요청입니다.");
        }

        String email = claims.get("email").toString();
        User user = authMapper.findUserByEmail(email);

        if(user.getEnabled() > 0) { // 이미 인증된 상태
            throw new AuthMailException("이미 인증이 완료된 요청입니다.");
        }

        return accountMapper.updateEnabledToEmail(email) > 0;
    }

    public boolean checkPassword(String password) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(passwordEncoder.matches(password, principalUser.getUser().getPassword())) {
            return true;
        }
        return false;
    }

    public int updatePassword(PasswordChangeReqDto passwordChangeReqDto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User newUser = principalUser.getUser();
        newUser.setPassword(passwordEncoder.encode(passwordChangeReqDto.getNewPassword()));
        return accountMapper.updatePassword(newUser);
    }

    public TripRespDto getTrips() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = principalUser.getUser().getUserId();
        List<Trip> trips = accountMapper.findTripsByUserId(userId);
        return new TripRespDto(trips);
    }

    public int deleteTrip(int tripId) {
        return accountMapper.deleteTrip(tripId);
    }

    @Transactional
    public ShareTripRespDto shareTrip(int userId, int tripId, ShareTripReqDto ShareTripReqDto){

        // 1. 여행 소유자 검증
        User owner = accountMapper.findOwnerByTripId(tripId)
                    .orElseThrow(() -> new TripNotFoundException(tripId));

        // 1-1. 현재 사용자와 소유자 일치 여부 확인
        if (owner.getUserId() != userId) {
            throw new UnauthorizedAccessException("해당 여행을 공유할 권한이 없습니다.");
        }

        // 2. 이미 공유된 이메일 목록 조회
        Set<String> alreadySharedEmails = getSharedUsers(userId, tripId)
            .stream()
            .map(TripShareDto::getEmail)
            .collect(Collectors.toSet());

        // 3. 중복 제거된 실제로 공유할 이메일만 추리기
        List<String> emails = ShareTripReqDto.getEmails()
            .stream()
            .filter(email -> !alreadySharedEmails.contains(email))
            .collect(Collectors.toList());
        System.out.println(emails);

        String message = ShareTripReqDto.getMessage();

        // 4. 유저 정보 bulk 조회 (이메일에 해당하는 유저 정보 조회 - 가입자만 나옴)
        List<User> emailIdPairs = accountMapper.findUserIdByEmails(emails);
        Map<String, Integer> emailToUserId = emailIdPairs.stream()
            .collect(Collectors.toMap(User::getEmail, User::getUserId));

        // 5. 이메일 전송 결과 수집
        Map<String, Boolean> emailSendResults = sendInvitationEmail(emails, owner.getEmail(), message);

        // 6. 전송 성공 이메일만 DB에 저장
        List<String> failedEmails = new ArrayList<>();

        for (String email : emails) {
            if (Boolean.TRUE.equals(emailSendResults.get(email))) {
                Integer sharedWithUserId = emailToUserId.get(email); // 비가입자는 null

                TripShare share = TripShare.builder()
                    .tripId(tripId)
                    .sharedByUserId(userId)
                    .sharedWithUserId(sharedWithUserId)
                    .email(email)
                    .build();

                accountMapper.insertTripShare(share);
            } else {
                failedEmails.add(email);
            }
        }
        return new ShareTripRespDto(emails.size() - failedEmails.size(), failedEmails);
    }

    public Map<String, Boolean> sendInvitationEmail(List<String> toEmails, String fromEmail, String message) {
        Map<String, Boolean> resultMap = new HashMap<>();

        for (String toEmail : toEmails) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

                helper.setTo(toEmail);
                helper.setSubject("여행 공유 초대장");

                String htmlContent = "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 700px; margin: auto; padding: 30px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;\">" +
                        "<h2 style=\"color: #2c3e50; font-size: 24px; text-align: center;\">" +
                        fromEmail + "님이 당신을 여행에 초대하셨습니다! ✈️</h2>" +

                        "<p style=\"font-size: 16px; color: #555; margin-top: 30px;\">" +
                        "안녕하세요, " + toEmail + "님!<br><br>" +
                        fromEmail + "님이 아래의 메시지와 함께 여행 초대를 보내셨습니다.</p>" +

                        "<div style=\"background-color: #ffffff; padding: 20px; margin: 20px 0; border-left: 4px solid #4CAF50;\">" +
                        "<p style=\"margin: 0; font-style: italic; color: #333;\">" + message + "</p>" +
                        "</div>" +

                        "<p style=\"font-size: 16px; color: #555;\">아래 버튼을 클릭하여 초대장을 확인해 보세요:</p>" +

                        "<div style=\"text-align: center; margin-top: 20px;\">" +
                        "<a href=\"http://localhost:3000/account/mypage?selectedTab=share\" " +
                        "style=\"display: inline-block; padding: 12px 24px; background-color: #4CAF50; color: white; text-decoration: none; font-weight: bold; border-radius: 5px; font-size: 16px;\">" +
                        "초대장 확인하기</a>" +
                        "</div>" +

                        "<p style=\"font-size: 14px; color: #999; margin-top: 40px; text-align: center;\">" +
                        "이 이메일은 여행 공유 서비스에서 발송되었습니다.</p>" +
                        "</div>";

                helper.setText(htmlContent, true); // true → HTML 형식 사용

                javaMailSender.send(mimeMessage);
                resultMap.put(toEmail, true);
            } catch (Exception e) {
                resultMap.put(toEmail, false);
            }
        }
        return resultMap; // key: 이메일, value: 성공 여부
    }

    public List<TripShareDto> getSharedUsers(int userId, int tripId) {
        List<TripShare> sharedUsers = accountMapper.getSharedUsers(userId, tripId);
        return sharedUsers.stream()
                .map(TripShare::toTripShareDto)
                .collect(Collectors.toList());
    }

    public boolean cancelShare(int userId, int tripId, int shareId) {
        return accountMapper.cancelShare(userId, tripId, shareId);
    }

    public TripRespDto getReceivedInvitations(String email) {
        List<Trip> receivedInvitations = accountMapper.getReceivedInvitations(email);
        return new TripRespDto(receivedInvitations);
    }
}
