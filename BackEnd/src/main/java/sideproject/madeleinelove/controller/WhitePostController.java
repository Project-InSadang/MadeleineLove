package sideproject.madeleinelove.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import sideproject.madeleinelove.service.WhitePostService;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;

@RestController
public class WhitePostController {
    @Autowired
    private WhitePostService whitePostService;

    @PostMapping("/white")
    public ResponseEntity<WhitePost> createWhitePost(
            @RequestHeader("userId") String userId,
            @Valid @RequestBody WhiteRequestDto whiteRequestDto) {
        WhitePost savedWhitePost = whitePostService.saveWhitePost(userId, whiteRequestDto);
        return new ResponseEntity<>(savedWhitePost, HttpStatus.CREATED);
    }
}