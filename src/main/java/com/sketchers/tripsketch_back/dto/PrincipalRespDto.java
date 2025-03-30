package com.sketchers.tripsketch_back.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
public class PrincipalRespDto {

    private int userId;
    private String email;
    private String password;
    private String oauth2Id;
    private String provider;
    private int enabled;
}
