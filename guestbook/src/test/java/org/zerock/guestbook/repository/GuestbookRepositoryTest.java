package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTest {
    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test // 300개의 테스트 데이터 넣기
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("Write..." + i)
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test // 수정 시간 테스트
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        // 존재하는 번호로 테스트
        if (result.isPresent()) {
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");
            guestbookRepository.save(guestbook);
        }
    }

    @Test // 단일 항목 검색 테스트 : 제목에 "1"이라는 글자가 있는 엔티티 검색
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        String keyword = "1";

        QGuestbook qGuestbook = QGuestbook.guestbook; // 1번
        BooleanBuilder builder = new BooleanBuilder(); // 2번
        BooleanExpression expression = qGuestbook.title.contains(keyword); // 3번
        builder.and(expression); // 4번
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); // 5번

        for (Guestbook guestbook : result) {
            System.out.println(guestbook);
        }
    }

    @Test // 다중 항목 검색 테스트
    public void testQuery2() {
        String keyword = "1";
        // 10개의 데이터를 보겠다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;
        BooleanBuilder builder = new BooleanBuilder(); // where 문에 들어가는 조건을 넣어주는 컨테이너

        // title 이나 content 에 "1" 이란 키워드가 들어가 있고 gno(PK) 가 0보다 큰 조건을 검색
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        for (Guestbook guestbook : result) {
            System.out.println(guestbook);
        }
    }
}













