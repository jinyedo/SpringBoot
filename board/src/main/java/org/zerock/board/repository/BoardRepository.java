package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    // Board 와 Board.writer
    @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno = :bno")
    // :파라미터 이름
    Object getBoardWithWriter(@Param("bno") Long bno); // 한개의 로우(Object) 내에 Object[ ]로 나옴

    // Board 와 Reply
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno ")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 페이징 처리
    // 목록 화면에 필요한 데이터
    @Query(value =
            "SELECT b, w, COUNT(r) " +
                    "FROM Board b " +
                    "LEFT JOIN b.writer w " +
                    "LEFT JOIN Reply  r " +
                    "ON r.board = b " +
                    "GROUP BY b",
            countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT b, w, COUNT(r) " +
            "FROM Board b " +
            "LEFT JOIN b.writer w " +
            "LEFT OUTER JOIN Reply r " +
            "ON r.board = b " +
            "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}

