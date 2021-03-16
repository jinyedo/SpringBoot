package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.service.NoteService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    // 노트 등록 처리
    /* @RequestBody
     클라이언트가 전송하는 Http 요청의 Body 내용을 Java Object 로 변환시켜주는 역할을 한다
     POST 방식으로 Json 형태로 넘겨온 데이터를 객체로 바인딩하기 위해 사용할 수 있다
    */
    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
        log.info("----------노트 등록 처리----------");
        log.info("noteDTO : " + noteDTO);

        Long num = noteService.register(noteDTO);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    // 특정 번호의 노트 조회
    @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {
        log.info("----------" + num + "번 노트 조회 처리----------");
        log.info("num : " + num);
        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    // 특정 회원의 모든 노트 조회
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email) {
        log.info("----------" + email + "회원의 모든 노트 조회 처리----------");
        log.info("email : " + email);
        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
    }

    // 노트 삭제 처리
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num) {
        log.info("----------" + num + "번 노트 삭제 처리----------");
        log.info("num : " + num);

        noteService.remove(num);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    // 노트 수정 처리
    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {
        log.info("----------" + noteDTO.getNum() + "번 노트 수정 처리----------");
        log.info("noteDTO : " + noteDTO);

        noteService.modify(noteDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }
}







