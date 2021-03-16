package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.ReplyDTO;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testGetList() {
        Long bno = 4L; // 데이터베이스에 존재하는 번호
        List<ReplyDTO> replyDTOList = replyService.getList(bno);
        replyDTOList.forEach(System.out::println);
    }
}


