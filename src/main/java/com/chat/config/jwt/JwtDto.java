package com.chat.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtDto {

    private String grantType; // Bearer 사용
    private String accessToken;
}