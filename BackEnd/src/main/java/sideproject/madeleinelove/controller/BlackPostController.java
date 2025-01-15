package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.service.BlackPostService;

@RestController
@RequestMapping("/api/v1")
public class BlackPostController {

    @Autowired
    private BlackPostService blackPostService;

    @PostMapping("/black")
    public ResponseEntity<ApiResponse<BlackPost>> post(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestHeader("Authorization") String authorizationHeader,
                                                      @Valid @RequestBody BlackPostDto blackPostDto) {

        BlackPost savedBlackPost = blackPostService.saveBlackPost(request, response, authorizationHeader, blackPostDto);
        return ApiResponse.onSuccess(SuccessStatus._CREATED);
    }

}
