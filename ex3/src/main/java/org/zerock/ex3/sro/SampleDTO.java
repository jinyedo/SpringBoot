package org.zerock.ex3.sro;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Getter/Setter, toString(), equals(), hashCode() 를 자동으로 생성
@Builder(toBuilder = true)
public class SampleDTO {
    private Long sno;
    private String first;
    private String last;
    private LocalDateTime regTime;
}




















