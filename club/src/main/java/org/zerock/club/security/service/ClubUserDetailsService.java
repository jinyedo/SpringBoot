package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service // @Service 를 이용해 자동으로 스프링에서 빈으로 처리될 수 있게 설정 - 스프링시큐리티에서 자동으로 UserDetailsService 로 인식
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();
        log.info("------------------------------");
        log.info("clubMember : " + clubMember);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFormSocial(),
                clubMember.getRoleSet().stream().map(role ->
                    new SimpleGrantedAuthority("ROLE_" + role.name())
                ).collect(Collectors.toSet())
        );

        clubAuthMemberDTO.setName(clubMember.getName());
        clubAuthMemberDTO.setFormSocial(clubMember.isFormSocial());

        return clubAuthMemberDTO;
    }
}


