package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service // 스프링에서 빈으로 처리
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {
    private final GuestbookRepository repository; // 반드시 final 로 설정

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        // 페이징 설정
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO);
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        // Entity -> DTO 변환
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        // JPA 처리 결과인 Page<E> 과 Function 을 전달해
        // 엔티티 -> DTO 의 리트스로 변환하고 | 화면에 필요한 페이지 처리화 필요한 값들을 생성
        return new PageResultDTO<>(result, fn);
    }

    // 게시글 등록
    @Override // DTO 객체를 Entity 객체로 변환
    public Long register(GuestbookDTO dto) {
        log.info("DTO-------------------------------");
        log.info(dto);
        // DTO 를 Entity 로 변환
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity); // Entity 객체 저장
        return entity.getGno(); // 저장된 Entity 객체의 PK 반환
    }

    // 게시글 조회(읽기)
    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    // 게시글 삭제
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    // 게시글 수정
    @Override
    public void modify(GuestbookDTO dto) {
        // 업데이트 하려는 항목은 제목, 내용
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if (result.isPresent()) {
            Guestbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }

    // 게시글 검색
    // 동적 처리를 위한 Querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건만 생성
        booleanBuilder.and(expression);

        // 검색 조건이 없는 경우
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) { // 제목
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        } else if (type.contains("c")) { // 내용
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        } else if (type.contains("w")) { // 작성자
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder); // 모든 조건 통함
        return booleanBuilder;
    }
}















