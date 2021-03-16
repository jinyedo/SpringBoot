package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 전달
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list.........." + pageRequestDTO);
        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    // 게시물 등록 처리
    @GetMapping("/register") // 화면 보여주기 용도
    public void register() {
        log.info("regiser get...");
    }

    @PostMapping("/register") // 게시물 처리 후 목록 페이지로 이동
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto..." + dto);
        Long bno = boardService.register(dto);
        log.info("BNO : " + bno);
        // addFlashAttribute() -> 단 한번만 데이터를 전달하는 용도로 사용
        // msg 를 이용해 게시글 등록 후 화면에 모달창을 보여주는 용도로 사용
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }

    // 게시물 조회 처리 : /read
// 게시물 수정 처리 : /modify
    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {
        log.info("bno : " + bno);
        BoardDTO boardDTO = boardService.get(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("post modify...................................");
        log.info("dto : " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());
        return "redirect:/board/read";
    }

    // 게시물 삭제 처리
    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes) {
        log.info("bno : " + bno);
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }
}


