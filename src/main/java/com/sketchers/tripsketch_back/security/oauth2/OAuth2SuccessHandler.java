package com.sketchers.tripsketch_back.security.oauth2;

import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.jwt.JwtProvider;
import com.sketchers.tripsketch_back.repository.AuthMapper;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${server.frontAddress}")
    private String frontAddress;

    private final AuthMapper authMapper;
    private final JwtProvider jwtProvider;
    private final FirebaseService firebaseService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        User user = authMapper.findUserByEmail(email);

        if(user == null) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String provider = defaultOAuth2User.getAttributes().get("provider").toString();
            String oauth2Id = defaultOAuth2User.getAttributes().get("oauth2Id").toString();

            user = User.builder()
                    .email(email)
                    .oauth2Id(oauth2Id)
                    .provider(provider)
                    .build();
            authMapper.saveUser(user);
        }

        PrincipalUser principalUser = new PrincipalUser(user);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
        String accessToken = jwtProvider.generateToken(authenticationToken);

        String firebaseToken = firebaseService.createFirebaseTokenWithClaims(principalUser.getUser().getEmail());

        String redirectUrl = String.format(
                frontAddress + "/auth/oauth2/signin?token=%s&firebaseToken=%s",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode(firebaseToken, "UTF-8")
        );

        response.sendRedirect(redirectUrl);
    }
}
