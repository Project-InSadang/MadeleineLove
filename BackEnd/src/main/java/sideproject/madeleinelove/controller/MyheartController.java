package sideproject.madeleinelove.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.dto.PostDTO;
import sideproject.madeleinelove.service.MyheartService;

import java.util.List;

@RestController
@RequestMapping("/api/myheart")
public class MyheartController {

    @Autowired
    private MyheartService myheartService;

    @GetMapping("/whiteposts")
    public List<PostDTO> getWhitePosts(@RequestHeader("userId") String userId) {
        return myheartService.getPostsByUserId(userId, true);
    }

    @GetMapping("/blackposts")
    public List<PostDTO> getBlackPosts(@RequestHeader("userId") String userId) {
        return myheartService.getPostsByUserId(userId, false);
    }
}
