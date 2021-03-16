package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

// Entity 클래스 타입 정보와 @Id 의 타입을 지정
public interface MemoRepository extends JpaRepository<Memo, Long> {

    // mno 를 기준으로 해서 between 구문을 사용하고 order bY 가 적용
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 쿼리 메서드와 Pageable 조합
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // 데이터 삭제
    void deleteMemoByMnoLessThan(Long num);

    // :파라미터이름
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno ")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    // ":파라미터 이름" 을 이용하는 경우 여러 개의 파라미터를 전달할 때 복잡해질 경우가 있다 생각한다면
    // :#{}를 이용해 객체 사용하기
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno} ")
    int updateMemoText(@Param("param") Memo memo);

    // @Query 와 페이징 처리
    @Query(value = "select m from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    // Object[] 리턴
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL 처리
    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}























