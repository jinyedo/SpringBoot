package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")
public class Poster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private String fname;

    // 포스터 순번
    private int idx;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

// Movie 객체에서 Poster 객체를 다룰 수 있도록
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

