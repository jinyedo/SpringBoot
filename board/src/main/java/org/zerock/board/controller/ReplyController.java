package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;


@RestController /* 모든 메서드의 리턴 타입은 기본으로 JSON 을 사용
메서드의 반환 타입은 ResponseEntity 개체를 이용 - HTTP 상태 코드 등을 같이 전달할 수 있다. */

@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 자동 주입을 위해 final

    // 댓글 등록 처리
    @PostMapping("")
    // @RequestBody - JSON 으로 들어노는 데이터를 자동으로 해당 타입의 객체로 매핑
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
        log.info(replyDTO);
        Long rno = replyService.register(replyDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    // 댓글 수정 처리
    @PutMapping("/{rno}")
    // @RequestBody - JSON 으로 들어노는 데이터를 자동으로 해당 타입의 객체로 매핑
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
        log.info(replyDTO);
        replyService.modify(replyDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 댓글 삭제 처리
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
        log.info("RNO : " + rno);
        replyService.remove(rno);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /* GetMapping()에는 URL 의 일부를 '{}'로 묶은 변수를 이용하는데 이는 메서드 내에서 @PathVariable 로 처리
       이를 이용하면 '/replies/board/100'과 같이 100이라는 데이터를변수 처리하는 것이 가능해진다. */
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
        log.info("bno : " + bno);
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }
}


