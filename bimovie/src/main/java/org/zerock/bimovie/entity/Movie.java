package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poster> posterList = new ArrayList<>();

    // Poster 추가
    // Poster 객체를 파라미터로 받아 Poster 객체의 Movie 를 현재 객체로 지정
    public void addPoster(Poster poster) {
        poster.setIdx(this.posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }

    // 특정 Poster 삭제
    // 해당 번호의 Poster 객체를 찾에서 postList 에서 삭제한다.
    public void removePoster(Long ino) {
        Optional<Poster> result = posterList.stream().filter(p -> p.getIno().equals(ino)).findFirst();

        result.ifPresent(p -> {
            p.setMovie(null); // 하위 엔티티를 삭제할 때 참조 관계 안전하게 끊어주기
            posterList.remove(p);
        });

        // 여러 Poster 중에서 하나를 삭제했기 때문에 기존의 Poster 객체 idx 의 값을 변경
        changeIdx();
    }

    // Poster idx 번호 변경
    private void changeIdx() {
        for (int i=0; i<posterList.size(); i++) {
            posterList.get(i).setIdx(i);
        }
    }
}

