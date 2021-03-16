package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    // 사용자 정보 및 권한 조회
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM ClubMember m " +
            "WHERE m.formSocial = :social AND m.email = :email")
    Optional<ClubMember> findByEmail(String email, boolean social);
}



