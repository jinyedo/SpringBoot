package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    // 리뷰 등록 처리
    Long register(ReviewDTO movieReviewDTO);

    // 리뷰 조회 처리
    List<ReviewDTO> getListOfMovie(Long mno);

    // 리뷰 수정 처리
    void modify(ReviewDTO movieReviewDTO);

    // 리뷰 삭제 처리
    void remove(Long reviewNum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO) {
        return Review.builder()
                .reviewnum(movieReviewDTO.getReviewNum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();
    }

    default ReviewDTO entityToDto(Review movieReview) {
        return ReviewDTO.builder()
                .reviewNum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getNickname())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();
    }
}
