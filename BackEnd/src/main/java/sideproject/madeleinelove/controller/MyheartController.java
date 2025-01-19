package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ListApiResponse;
import sideproject.madeleinelove.dto.PostDTO;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.MyheartService;
import sideproject.madeleinelove.service.TokenServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/myheart")
public class MyheartController {

    @Autowired
    private MyheartService myheartService;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @GetMapping("/white")
    public ResponseEntity<ListApiResponse<Object>> getWhitePosts(HttpServletRequest request, HttpServletResponse response,
                                                                 @Valid @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        List<PostDTO> myWhiteList = myheartService.getPostsByUserId(request, response, accessTokenToUse.getAccessToken(), true);

        return ListApiResponse.onSuccess(accessTokenToUse.isNewToken(), accessTokenToUse.getAccessToken(), myWhiteList);
    }

    @GetMapping("/black")
    public ResponseEntity<ListApiResponse<Object>> getBlackPosts(HttpServletRequest request, HttpServletResponse response,
                                                             @Valid @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        List<PostDTO> myBlackList = myheartService.getPostsByUserId(request, response, accessTokenToUse.getAccessToken(), false);

        return ListApiResponse.onSuccess(accessTokenToUse.isNewToken(), accessTokenToUse.getAccessToken(), myBlackList);
    }
}
