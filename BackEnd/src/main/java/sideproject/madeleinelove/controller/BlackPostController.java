package sideproject.madeleinelove.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.service.BlackPostService;

@RestController
// @RequestMapping("/api")
public class BlackPostController {

    @Autowired
    private BlackPostService blackPostService;

    @PostMapping("/black")
    public ResponseEntity<BlackPost> post(@RequestHeader("userId") String userId,
                                          @RequestBody BlackPostDto blackPostDto) {
        BlackPost blackPost = blackPostService.save(userId, blackPostDto);
        return ResponseEntity.ok(blackPost);
    }

}
