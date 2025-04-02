package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.exception.AuthMailException;
import com.sketchers.tripsketch_back.exception.SendMailException;
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
}
