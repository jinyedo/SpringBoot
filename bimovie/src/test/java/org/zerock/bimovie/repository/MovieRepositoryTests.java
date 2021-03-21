package org.zerock.bimovie.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.bimovie.entity.Movie;
import org.zerock.bimovie.entity.Poster;

@SpringBootTest
@Log4j2
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testInsert() {
        log.info("testInsert....................");

        Movie movie = Movie.builder().title("극한직업").build();
        movie.addPoster(Poster.builder().fname("극한직업포스터1.jpg").build());
        movie.addPoster(Poster.builder().fname("극한직업포스터2.jpg").build());

        movieRepository.save(movie);

        log.info(movie.getMno());
    }
}

