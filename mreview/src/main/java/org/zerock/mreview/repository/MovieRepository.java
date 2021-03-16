package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 페이징 처리
    /* 책에서는 해당 @Query 를 사용하지만 MySQL 오류로 인한 사용 불가
    @Query("SELECT m, mi, AVG(COALESCE(r.grade, 0)), COUNT(DISTINCT r) " +
            "FROM Movie m " +
            "LEFT OUTER JOIN MovieImage mi ON mi.movie = m " +
            "LEFT OUTER JOIN Review r ON r.movie = m " +
            "GROUP BY m")
     */
    /* 가장 나중에 추가된 이미지 가져오기
    @Query("SELECT m, MAX(mi), COUNT(r) FROM Movie m " +
            "LEFT OUTER JOIN MovieImage mi " +
                "ON mi.movie = m AND mi.inum = (" +
                    "SELECT MAX(mi2.inum) FROM MovieImage mi2 " +
                    "WHERE mi2.movie = m) " +
            "LEFT OUTER JOIN Review r " +
                "ON r.movie = m " +
            "GROUP BY m")
     */
    @Query("SELECT m, MAX(mi), AVG(COALESCE(r.grade, 0)), COUNT(DISTINCT r) " +
            "FROM Movie m " +
            "LEFT OUTER JOIN MovieImage mi ON mi.movie = m " +
            "LEFT OUTER JOIN Review r ON r.movie = m " +
            "GROUP BY m")
    Page<Object[]> getListPage(Pageable pageable);

    // 특정 영화 조회
    @Query("SELECT m, mi, AVG(COALESCE(r.grade, 0)), COUNT(r) " +
            "FROM Movie m " +
            "LEFT OUTER JOIN MovieImage mi ON mi.movie = m " +
            "LEFT OUTER JOIN Review r ON r.movie = m " +
            "WHERE m.mno = :mno " +
            "GROUP BY mi")
    List<Object[]> getMovieWithAll(Long mno);
}





