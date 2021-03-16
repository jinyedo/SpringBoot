package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
// UploadResultDTO 는 실제 파일과 관련된 모든 정보를 가지는데 나중에 전체 경로가 필요한 경우를 대비해서 getImageURL( ) 을 제공한다.
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;
    private String folderPath;

    public String getImageURL() {
        try {
            // URLEncoder 클래스는 일반 문자열을 웹에서 통용되는 'x-www-form-urlencoded' 형식으로 변환하는 역할을 담당
            // 대소문자, 숫자, 밑줄을 제외한 URL 에 있는 문자를 코드화하는 것
            return URLEncoder.encode(
                    folderPath + "/" + uuid + "_" + fileName, StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(
                    folderPath + "/s_" + uuid + "_" + fileName, StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}





