package org.zerock.mreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA 를 이용하면서 AuditionEntityListener 를 활성화 시키기 위해
public class MreviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(MreviewApplication.class, args);
    }

}



