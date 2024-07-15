package sideproject.madeleinelove.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import sideproject.madeleinelove.service.WhitePostService;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WhitePostController {
    @Autowired
    private WhitePostService whitePostService;

    @PostMapping("/white")
    public ResponseEntity<?> createWhitePost(
            @RequestHeader("userId") String userId,
            @Valid @RequestBody WhiteRequestDto whiteRequestDto,
            BindingResult bindingResult) {

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        WhitePost savedWhitePost = whitePostService.saveWhitePost(userId, whiteRequestDto);
        return new ResponseEntity<>(savedWhitePost, HttpStatus.CREATED);
    }
}