package org.zerock.club.security.filter;

import io.jsonwebtoken.Jwt;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("----------ApiLoginFilter----------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = "1111";

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, pw);

        return getAuthenticationManager().authenticate(authToken);

//        if (email == null) {
//            throw new BadCredentialsException("email cannot be null");
//        }
//
//        return null;
    }

    // ApiLoginFilter(localhost:8080/api/login)로 로그인 성공 시 처리
    @Override
    protected void successfulAuthentication
            // Authentication : 로그인을 성공한 사용자의 인증 정보를 가지고 있는 Authentication 객체
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("----------ApiLoginFilter----------");
        log.info("successfulAuthentication : " + authResult);
        log.info(authResult.getPrincipal());

        // email address
        String email = ((ClubAuthMemberDTO) authResult.getPrincipal()).getUsername();
        String token = null;

        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



