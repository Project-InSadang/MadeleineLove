package sideproject.madeleinelove.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import sideproject.madeleinelove.service.WhitePostService;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WhitePostController {
    private final WhitePostService whitePostService;

    public WhitePostController(WhitePostService whitePostService) {
        this.whitePostService = whitePostService;
    }

    @PostMapping("/white")
    public ResponseEntity<?> createWhitePost(
            @RequestHeader("userId") String userId,
            @Valid @RequestBody WhiteRequestDto whiteRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            WhitePost savedWhitePost = whitePostService.saveWhitePost(userId, whiteRequestDto);
            return new ResponseEntity<>(savedWhitePost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}