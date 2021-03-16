package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;
import net.coobird.thumbnailator.Thumbnailator;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
// 모든 메서드의 리턴 타입은 기본으로 JSON 을 사용
// 메서드의 반환 타입은 ResponseEntity 개체를 이용(HTTP 상태 코드 등을 같이 전달할 수 있다.)
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}") // application.properties 의 변수
    private String uploadPath;

    // 날짜 폴더 생성
    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("//", File.separator); // ex) 2021/02/17
        log.info("folderPath : " + folderPath);
        // make folder
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    // 파일을 받아와 폴더 생성 및 파일 저장
    @PostMapping("/uploadAjax")
    // MultipartFile[]을 이용하면 동시에 여러 개의 파일 정보를 처리/업로드 할 수 있다.
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            // 이미지 파일만 업로드 가능
            if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
                log.warn("this file is not image type");
                // HTTP 403 Forbidden 클라이언트 오류 상태 응답 코드는 서버에 요청이 전달되었지만, 권한 때문에 거절되었다는 것을 의미
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE 나 Edge 는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            log.info("originalName : " + originalName);
            assert originalName != null;
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("fileName : " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_"를 이용해서 구분
            // File.separator = 개발 환경에 맞춰 "\\ or /" 을 적용
            String saveName =
                    uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            // 경로 설정
            Path savePath = Paths.get(saveName);

            try {
                // 원본 파일 저장
                uploadFile.transferTo(savePath);

                // 섬네일 생성
                String thumbnailSaveName =
                        uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// end for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    // 브라우저에 파일 출력
    @GetMapping("/display")
    // URL 인코딩된 파일 이름을 파라미터로 받아와 해당 파일을 byte[]로 만들어 브라우저에 전송
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            log.info("fileName : " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file : " + file);

            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders header = new HttpHeaders();

            // MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    // 파일 삭제
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

