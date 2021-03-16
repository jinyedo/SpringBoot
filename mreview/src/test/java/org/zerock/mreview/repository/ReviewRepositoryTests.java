package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test // 200개의 리뷰 등록
    public void insertMovieReviews() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long)(Math.random()*100) + 1;
            // 리뷰 번호
            Long mid = (long)(Math.random()*100) + 1;
            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random()*5) + 1)
                    .text("이 영화에 대한 느낌..." + i)
                    .build();

            reviewRepository.save(review);
        });
    }

    @Test
    public void testGetMovieReviews() {
        Movie movie = Movie.builder().mno(100L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {
            System.out.println(movieReview.getReviewnum());
            System.out.println("\t" + movieReview.getGrade());
            System.out.println("\t" + movieReview.getText());
            System.out.println("\t" + movieReview.getMember().getEmail());
        });
    }

    @Autowired
    private MemberRepository memberRepository;

    @Commit
    @Transactional
    @Test
    public void testDeleteMember() {
        Long mid = 1L;
        Member member = Member.builder().mid(mid).build();
        /* 기존
        memberRepository.deleteById(mid); // 여기서 문제 발생
        reviewRepository.deleteByMember(member); */

        // 순서 주의
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }
}


