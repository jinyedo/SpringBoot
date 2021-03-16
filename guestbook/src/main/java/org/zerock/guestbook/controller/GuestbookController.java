package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 Annotation
public class GuestbookController {

    private final GuestbookService service; // final 로 선언

    // 게시글 목록 전달
    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list.........." + pageRequestDTO);
        // Model 은 결과 데이터를 화면에 전달하기 위해 사용
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    // 게시글 등록처리
    @GetMapping("/register") // 화면 보여주기 용도
    public void register() {
        log.info("regiser get...");
    }

    @PostMapping("/register") // 처리 후 목록 페이지로 이동
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto..." + dto);
        // 새로 추가된 Entity 번호
        Long gno = service.register(dto);
        // addFlashAttribute() -> 단 한번만 데이터를 전달하는 용도로 사용
        // msg 를 이용해 게시글 등록 후 화면에 모달창을 보여주는 용도로 사용
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }

    // read : 게시글 조회 처리 | modify : 게시글 수정/삭제 페이지 이동 처리
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("gno: " + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }

    // 게시글 삭제 처리
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {
        log.info("gno: " + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }

    // 게시글 수정 처리
    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("post modify.....................................");
        log.info("dto: " + dto);
        service.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getPage());
        redirectAttributes.addAttribute("keyword", requestDTO.getPage());
        redirectAttributes.addAttribute("gno", dto.getGno());
        return "redirect:/guestbook/read";
    }
}
















