package sideproject.madeleinelove.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.dto.WhitePostDTO;
import sideproject.madeleinelove.service.MyheartService;

import java.util.List;

@RestController
@RequestMapping("/api/myheart")
public class MyheartController {

    @Autowired
    private MyheartService myheartService;

    @GetMapping("/whiteposts")
    public List<WhitePostDTO> getWhitePosts(@RequestHeader("userId") String userId) {
        return myheartService.getWhitePostsByUserId(userId);
    }
}
