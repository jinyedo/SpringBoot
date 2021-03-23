package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "posterList")
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;

    @Builder.Default // Movie 객체가 JPA 에서 엔티티 매니저에 의해서 관리될 때 변수에 할당된 ArrayList 객체도 같이 보관하도록
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Poster> posterList = new ArrayList<>();

    // Poster 추가
    // Poster 객체를 파라미터로 받아 Poster 객체의 Movie 를 현재 객체로 지정
    public void addPoster(Poster poster) {
        poster.setIdx(this.posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }
}

