package com.sketchers.tripsketch_back.jwt;

import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.repository.AuthMapper;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final AuthMapper authMapper;
    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret, @Autowired AuthMapper authMapper) {
        this.authMapper = authMapper;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /** ✅ Access Token 생성 (24시간 유효) */
    public String generateToken(Authentication authentication) {
        PrincipalUser principal = (PrincipalUser) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject("AccessToken")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .claim("oauth2Id", authentication.getName())
                .claim("email", principal.getUser().getEmail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ 토큰에서 Claims 파싱 */
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    /** ✅ Bearer 헤더에서 순수 토큰 추출 */
    public String getToken(String bearerToken) {
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }

    /** ✅ 토큰으로부터 인증 객체(Authentication) 생성 */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if (claims == null) return null;

        User user = claims.get("oauth2Id") != null
                ? authMapper.findUserByOauth2Id(claims.get("oauth2Id").toString())
                : authMapper.findUserByEmail(claims.get("email").toString());

        if (user == null) return null;

        PrincipalUser principalUser = new PrincipalUser(user);
        return new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
    }
}