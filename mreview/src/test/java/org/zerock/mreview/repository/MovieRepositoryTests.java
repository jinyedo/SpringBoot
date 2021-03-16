package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Commit
    @Transactional
    @Test
    public void insertMovies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder().title("Movie....." + i).build();

            movieRepository.save(movie);

            int count = (int)(Math.random() * 5) + 1; // 0 ~ 4

            for (int j=0; j<count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg").build();

                movieImageRepository.save(movieImage);
            }
        });
    }

    // 페이징 테스트
    @Test
    public void testListPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);
        for(Object[] objects : result.getContent()) {
            System.out.println(Arrays.toString(objects));
        }
    }

    // 특정 영화 조회 테스트
    @Test
    public void testGetMovieWithAll() {
        List<Object[]> result = movieRepository.getMovieWithAll(100L);
        System.out.println(result);
        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }
}



