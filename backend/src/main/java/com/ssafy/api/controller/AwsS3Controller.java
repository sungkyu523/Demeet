package com.ssafy.api.controller;

import com.ssafy.api.request.ImageUploadReq;
import com.ssafy.api.service.AwsS3Service;
import com.ssafy.api.service.ProjectsService;
import com.ssafy.api.service.UsersService;
import com.ssafy.common.auth.SsafyUsersDetails;
import com.ssafy.common.customException.NotImageException;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.ProfileImagePath;
import com.ssafy.db.entity.Projects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@RestController
@RequestMapping("/S3")
@RequiredArgsConstructor
public class AwsS3Controller {
    // 필요할거 같은 기능? 이미지 업로드, 가져오기, 삭제

    @Autowired
    private final AwsS3Service awsS3Service;

    @Autowired
    private final UsersService usersService;

    @Autowired
    private final ProjectsService projectsService;

    @PostMapping("/profile")
    public ResponseEntity<BaseResponseBody> uploadProfileImage(@ApiIgnore Authentication authentication, @ModelAttribute ImageUploadReq imageUploadReq){
        SsafyUsersDetails usersDetails = (SsafyUsersDetails) authentication.getDetails();
        long uid = usersService.getUsersByUserEmail(usersDetails.getUsername()).getUid();
        // 이미지 업로드
        try {
            // 파일, id, 프로필/드로잉 구분
            String path = awsS3Service.putImage(imageUploadReq.getMultipartFile(), uid, "profile");
            // 성공시 db에 정보 넣기
            System.out.println(path);
            ProfileImagePath profileImagePath = awsS3Service.saveImagePath(path, uid);
            profileImagePath.toString();
        }catch (IOException e){
            return ResponseEntity.status(422).body(BaseResponseBody.of(422, e.getMessage()));
        }catch (NotImageException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(BaseResponseBody.of(HttpStatus.NOT_ACCEPTABLE.value(), "the File is NOT image"));
        };
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Image upload Success"));
    }

    @PostMapping("/drawing")
    public ResponseEntity<BaseResponseBody> uploadDrawingImage(@ApiIgnore Authentication authentication, @ModelAttribute ImageUploadReq imageUploadReq){
        SsafyUsersDetails usersDetails = (SsafyUsersDetails) authentication.getDetails();
        long uid = usersService.getUsersByUserEmail(usersDetails.getUsername()).getUid();
        long pid = projectsService.get
        try {
            // 파일, id, 프로필/드로잉 구분
            String path = awsS3Service.putImage(imageUploadReq.getMultipartFile(), uid, "drawing");
            // 성공시 db에 정보 넣기
            System.out.println(path);
            ProfileImagePath profileImagePath = awsS3Service.saveImagePath(path, uid);
            profileImagePath.toString();
        }catch (IOException e){
            return ResponseEntity.status(422).body(BaseResponseBody.of(422, e.getMessage()));
        }catch (NotImageException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(BaseResponseBody.of(HttpStatus.NOT_ACCEPTABLE.value(), "the File is NOT image"));
        };
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Image upload Success"));
    }
}
