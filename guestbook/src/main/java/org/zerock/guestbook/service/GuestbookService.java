package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    Long register(GuestbookDTO dto); // 게시글 등록

    GuestbookDTO read(Long gno); // 게시글 조회(읽기)

    void remove(Long gno); // 게시글 삭제

    void modify(GuestbookDTO dto); // 게시글 수정

    // DTO 를 Entity 로 변환
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    // Entity 를 DTO 로 변환
    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
}




