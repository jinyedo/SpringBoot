package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    public ApiCheckFilter(String pattern) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info("RequestURI : " + request.getRequestURI());;
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        // 패턴이 맞는 경우 다른 동작을 하도록
        if (antPathMatcher.match(pattern, request.getRequestURI())) {
            log.info("ApiCheckFilter..............................");

            boolean checkHeader = checkAuthHeader(request);
            if(checkHeader) { // Authorization 헤더값이 정상적일 경우
                // 다음 필터 동작
                filterChain.doFilter(request, response);
            } else { // false 일 경우 403 에러메시지 전송
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴 및 한글깨짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
            }
            return;
        }

        // doFilter()는 다음 필터의 단계로 넘어가는 역할을 위해서 필요 - 작성된 필터는 SecurityConfig 를 통해 스프링 빈으로 설정
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더의 값이 정상적인지 확인하기
    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader)) {
            log.info("Authorization exist : " + authHeader);
            if (authHeader.equals("123456")) {
                checkResult = true;
            }
        }
        log.info("checkResult : " + checkResult);
        return checkResult;
    }
}


