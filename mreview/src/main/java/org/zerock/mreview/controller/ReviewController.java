package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록 처리 - 생성된 리뷰 번호 반환
    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("----------리뷰 등록 처리----------");
        log.info("reviewDTO : " + reviewDTO);

        Long reviewNum = reviewService.register(reviewDTO);
        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

    // 리뷰 조회 처리 - ReviewDTO 리스트 반환
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno) {
        log.info("----------" + mno + "번 영화 리뷰 조회----------");

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    // 리뷰 수정 처리 - 리뷰의 수정 성공 여부 반환
    @PutMapping("/{mno}/{reviewNum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewNum, @RequestBody ReviewDTO reviewDTO) {
        log.info("----------리뷰 수정 처리----------");
        log.info("reviewDTO : " + reviewDTO);

        reviewService.modify(reviewDTO);
        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

    // 리뷰 삭제 처리
    @DeleteMapping("/{mno}/{reviewNum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewNum) {
        log.info("----------리뷰 삭제 처리----------");
        log.info("reviewNum : " + reviewNum);

        reviewService.remove(reviewNum);
        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }
}


