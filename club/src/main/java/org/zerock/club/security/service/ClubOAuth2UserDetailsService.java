package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service // @Service 를 통해 자동으로 스프링의 빈으로 등록
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    // 소셜 로그인한 이메일을 처리
    private ClubMember saveSocialMember(String email) {
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        if (result.isPresent()) {
            return result.get();
        }

        // 없다면 회원 추가 패스워드는 1111 | 이름은 그냥 이메일 주소로
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .formSocial(true)
                .build();

        // USER 라는 권한 부여
        clubMember.addMemberRole(ClubMemberRole.USER);

        clubMemberRepository.save(clubMember);

        return clubMember;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("------------------------------");
        log.info("userRequest : " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName : " + clientName); // OAuth 로 연결한 클라이언트 이름 - Google
        log.info(userRequest.getAdditionalParameters()); // 연결때 사용한 파라미터

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        // 처리 결과로 나오는 OAuth2User 객체의 내부에 값을 확인
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " : " + v);
        });

        String email = null;

        // Google 을 이용하는 경우
        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }
        log.info("EMAIL : " + email);

        ClubMember member = saveSocialMember(email);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        clubAuthMemberDTO.setName(member.getName());

        return clubAuthMemberDTO;
    }
}




