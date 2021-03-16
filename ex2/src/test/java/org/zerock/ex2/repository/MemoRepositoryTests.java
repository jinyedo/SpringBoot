package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    /* CRUD */
    @Test
    // Repository 가 정상적으로 스프링에서 처리되고 의존성 주입에 문제가 없는지를 먼저 확인하는 것이 좋다.
    // testClass()는 Repository 인터페이스 타입의 실제 객체가 어떤 것인지 확인한다.
    // 스프링이 내부적으로 해당 클래스를 자동으로 생성하는데(AOP 기능) 이때 클래스 이름을 확인해 보고자 한다.
    public void testClass() {
        // 작성한 적이 없는 클래스의 이름이 출력된다. (동적 프록시라는 방식으로 만들어진다.)
        System.out.println(memoRepository.getClass().getName()); // com.sun.proxy.$Proxy92
    }

    @Test
    public void testInsertDummies() {
        // 1 ~ 100까지 반복
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // Entity 클래스 Memo 의 memoText 에 반복숫자 저장 (데이터베이스에 저장됨)
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            // MemoRepository 에 저장
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        // 데이터 베이스에 존재하는 mon
        Long mno = 100L;
        // Wrapper Class 로 모든 타입의 참조 변수를 저장할 수 있다.
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("==========================================");
        if (result.isPresent()) { // 저장된 값이 존재하는지
            Memo memo = result.get(); // Optional 에 저장된 값을 반환
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("==========================================");
        System.out.println(memo);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mon = 100L;
        memoRepository.deleteById(mon);
    }

    /* 페이징 처리 */
    @Test
    public void testPageDefault() {
        // 1페이지 데이터 10개를 가져오기
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("-------------------------------------------------------------");

        // 총 몇 페이지
        System.out.println("Total Page: " + result.getTotalPages());

        // 전체 개수
        System.out.println("Total Count: " + result.getTotalElements());

        // 현재 페이지 번호 0부터 시작
        System.out.println("Page Number: " + result.getNumber());

        // 페이지당 데이터 개수
        System.out.println("Page Size: " + result.getSize());

        // 다음 페이지 존재 여부
        System.out.println("has next page?: " + result.hasNext());

        // 시작 페이지(0) 여부
        System.out.println("first page: " + result.isFirst());

        System.out.println("-------------------------------------------------------------");

        // 실제 페이지의 데이터를 처리
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    // 정렬 테스트
    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending(); // 역순 정렬
        Sort sort2 = Sort.by("memoText").ascending(); // 순차적 정렬
        Sort sortAll = sort1.and(sort2); // and 를 이용한 연결

        // 0부터 10개까지
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        // 실제 페이지의 데이터를 처리
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    /* 쿼리 메서드 */
    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        for (Memo memo : result) {
            System.out.println(memo);
        }
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        // 10보다 작은 데이터 삭제
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}




