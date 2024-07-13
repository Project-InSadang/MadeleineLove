package sideproject.madeleinelove.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.domain.BlackPost;
import sideproject.madeleinelove.service.BlackPostService;

@RestController
// @RequestMapping("/api")
public class BlackPostController {

    @Autowired
    private BlackPostService blackPostService;

    @PostMapping("/black")
    public ResponseEntity<BlackPost> post(@RequestBody BlackPost blackPost) {
        blackPostService.save(blackPost);
        return ResponseEntity.ok(blackPost);
    }

}
