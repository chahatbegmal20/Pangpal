package dev.n7meless.jwt;

import lombok.Getter;

public record Login(@Getter Jwt accessToken, @Getter Jwt refreshToken) {

    public static Login of(Long userId, String accessSecret, Long accessTokenValidity, String refreshSecret, Long refreshTokenValidity) {
        return new Login(
                Jwt.of(userId, accessTokenValidity, accessSecret),
                Jwt.of(userId, refreshTokenValidity, refreshSecret) //1440L - one day
        );
    }

    public static Login of(Long userId, String accessSecret, Long accessTokenValidity, Jwt refreshToken) {
        return new Login(
                Jwt.of(userId, accessTokenValidity, accessSecret),
                refreshToken
        );
    }
}
