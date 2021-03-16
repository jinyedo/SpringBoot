package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie") // 연관 관계 주의
public class MovieImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid; // java.util.UUID 를 이용해 고유한 번호를 생성해 사용

    private String imgName; // 이미지 이름

    private String path; // 이미지 저장 경로

    // movie 테이블 이 PK 를 가지고 movie_image 테이블  FK 를 가지므로 @ManyToOne 적용
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}



