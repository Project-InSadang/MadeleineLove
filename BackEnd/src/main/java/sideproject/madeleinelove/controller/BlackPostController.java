package sideproject.madeleinelove.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.PagedResponse;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.service.BlackPostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BlackPostController {

    private final BlackPostService blackPostService;

    public BlackPostController(BlackPostService blackPostService) {
        this.blackPostService = blackPostService;
    }

    @GetMapping("/black/post")
    public ResponseEntity<PagedResponse<BlackPostDto>> getPosts(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String userId) {
        List<BlackPostDto> dtos = blackPostService.getPosts(sort, cursor, size, userId);

        String nextCursor = blackPostService.getNextCursor(dtos, sort);

        PagedResponse<BlackPostDto> response = new PagedResponse<>();
        response.setData(dtos);
        response.setNextCursor(nextCursor);

        return ResponseEntity.ok(response);
    }

}
