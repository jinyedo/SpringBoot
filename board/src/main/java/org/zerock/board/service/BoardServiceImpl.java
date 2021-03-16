package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository; // 자동 주입 final
    private final ReplyRepository replyRepository;

    // 게시물 작성 기능
    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
    }

    // 게시물 목록 기능
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        // Entity -> BoardDTO 변환
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));

//        Page<Object[]> result = boardRepository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())
//        );

        // 변경된 부분
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result, fn);
    }

    // 게시물 조회 기능
    @Override
    public BoardDTO get(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    // 게시물 삭제 기능
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글 부터 삭제
        replyRepository.deleteByBno(bno);
        boardRepository.deleteById(bno);
    }

    // 게시물 수정 기능
    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.getOne(boardDTO.getBno());
        if (board != null) {
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            boardRepository.save(board);
        }
    }
}












