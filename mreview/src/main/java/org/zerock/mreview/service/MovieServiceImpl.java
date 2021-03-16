package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //  final 이나 @NonNull 인 필드 값만 파라미터로 받는 생성자를 만들어 준다.
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {
        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(imageRepository::save);

        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie)arr[0],
                (List<MovieImage>)(Collections.singletonList((MovieImage) arr[1])),
                (Double)arr[2],
                (Long)arr[3]
        ));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        // Movie 엔티티는 가장 앞에 존재 - 모든 Row 가 동일한 값
        Movie movie = (Movie) result.get(0)[0];

        // 영화의 이미지 개수만큼 MovieImage 객체 필요
        List<MovieImage> movieImageList = new ArrayList<>();

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; // 평균 평점 - 모든 Row 가 동일한 값
        Long reviewCnt = (Long) result.get(0)[3]; // 리뷰 개수 - 모든 Row 가 동일한 값

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}



