package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// @MappedSuperclass 어노테이션이 적용된 클래스는 테이블로 생성되지 않는다.
// 실제 테이블은 BaseEntity 클래스를 상속한 엔티티 클래스로 테이블이 생성
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter

// 시간 처리를 위한 BaseEntity 클래스
abstract class BaseEntity {
    @CreatedDate // Entity 가 생성되어 저장될 때 시간이 자동 저장된다.
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate // 조회한 Entity 의 값을 변경할 때 시간이 자동 저장된다.
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
