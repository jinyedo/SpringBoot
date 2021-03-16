package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 게시물 삭제 처리
    @Modifying // JPQL 을 이용해서 UPDATE, DELETE 를 실행하기 위해 추가
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteByBno(Long bno);

    // 게시물로 댓글 목록 가져오기 - Board 객체를 파라미터로 받고 모든 댓글을 순번대로 가져온다.
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}

