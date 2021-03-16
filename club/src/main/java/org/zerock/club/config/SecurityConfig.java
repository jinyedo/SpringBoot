package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubUserDetailsService;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 소셜 로그인 인증 이후 처리
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*");
    }

    @Bean
    // api/login URL 을 통해 외부에서 로그인이 가능하도록
    // -> 로그인이 성공한다면 클라이언트가 Authorization 의 헤더 값으로 이용할 데이터를 전송
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        // 인증 실패시 처리
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin(); // 인가/인증에 문제시 로그인 화면으로 이동
        http.csrf().disable(); // CSRF 토큰을 발해하지 않도록 지정
        http.logout(); // 로그아웃 설정
        // oauth2Login() : 로그인 시에 OAuth 를 사용한 로그인이 가능하도록 | .successHandler() : 인증 이후의 처리
        http.oauth2Login().successHandler(successHandler());
        // 자동 로그인 설정
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService); // 7days
        // ApiCheckFilter 동작 순서 변경하기 - UsernamePasswordAuthenticationFilter 이전에 동작하도록
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        // ApiLoginFilter 동작 순서 변경하기
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        // 사용자 계정은 user1
    //        auth.inMemoryAuthentication().withUser("user1")
    //        // 1111 패스워드 인코딩 결과
    //        .password("$2a$10$ByMdjEaujXSQjxvkn0//4uhNwkFVnJtANk7YAnOBKUVBrk67fFZcW")
    //        // USER 라는 권한 지정
    //        .roles("USER");
    //    }
}


