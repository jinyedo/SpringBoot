package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    // 로그인을 하지 않은 사용자도 접근가능
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll() {
        log.info("exAll..........");
    }

    // 로그인한 사용자만이 접근가능
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMember..........");
        log.info("clubAuthMemberDTO : " + clubAuthMemberDTO);
    }

    // 관리자 권한이 있는 사용자만이 접근가능
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin..........");
    }

    // 특정한 사용자만이 접근가능
    @PreAuthorize("#clubAuthMemberDTO != null && #clubAuthMemberDTO.username eq \"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMemberOnly..........");
        log.info("clubAuthMemberDTO : " + clubAuthMemberDTO);

        return "/sample/admin";
    }
}







