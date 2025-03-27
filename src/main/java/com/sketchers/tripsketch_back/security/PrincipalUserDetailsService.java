package com.sketchers.tripsketch_back.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrincipalUserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientName(); // Google, Kakao, Naver
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> userInfo = new HashMap<>();

        String email = null;
        String oauth2Id = null;

        if ("Google".equals(provider)) {
            email = (String) attributes.get("email");
            oauth2Id = (String) attributes.get("sub");
        } else if ("Kakao".equals(provider)) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) account.get("email");
            oauth2Id = String.valueOf(attributes.get("id"));
        } else if ("Naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            oauth2Id = (String) response.get("id");
        }

        userInfo.put("email", email);
        userInfo.put("oauth2Id", oauth2Id);
        userInfo.put("provider", provider);

        return new DefaultOAuth2User(new ArrayList<>(), userInfo, "email");
    }

}