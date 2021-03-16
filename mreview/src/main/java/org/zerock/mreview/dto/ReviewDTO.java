package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    // Movie
    private Long mno;

    // Member
    private Long mid;
    private String nickname;
    private String email;

    // review
    private Long reviewNum;
    private int grade;
    private String text;
    private LocalDateTime regDate, modDate;
}

