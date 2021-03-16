package org.zerock.club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private final String secretKey = "zerock123456";

    // JWT 토큰을 생성하는 역할
    public String generateToken(String content) throws Exception {

        long expire = 60 * 24 * 30; // 만료기간 : 30일

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                // sub 라는 이름을 가지는 Claim 에는 사용자의 이메일 주소를 입력해 주어서 나중에 사용할 수 있도록 구성
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // 인코딩된 문자열에서 원하는 값을 추출하는 용도
    public String validateAndExtract(String tokenStr) throws Exception {

        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(
                    secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("------------------------------");
            contentValue = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return contentValue;
    }
}


