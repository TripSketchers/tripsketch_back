package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.PrincipalRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private int userId;
    private String email;
    private String password;
    private String oauth2Id;
    private String provider;
    private int enabled;

    public PrincipalRespDto toPrincipalDto() {
        return PrincipalRespDto.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .oauth2Id(oauth2Id)
                .provider(provider)
                .build();
    }
}
