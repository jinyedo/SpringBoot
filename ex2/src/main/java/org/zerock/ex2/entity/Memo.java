package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity // Entity 클래스 지정

// Entity 클래스를 어떠한 테이블로 생성할 것인지에 대한 정보를 담기 위한 어노테이션
@Table(name = "tbl_memo") // 테이블명

@ToString

@Getter // Getter 메서드를 생성

@Builder // 객체를 생성할 수 있게 처리
// @Builder 를 이용하기 위해서 항상 같이 처리
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Id // Primary Key 설정
    // Primary Key 자동으로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    // 추가적인 필드(컬럼) 생성
    @Column(length = 200, nullable = false)
    private String memoText;
}


