package sideproject.madeleinelove.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.PagedResponse;
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.service.WhitePostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WhitePostController {

    private final WhitePostService whitePostService;

    public WhitePostController(WhitePostService whitePostService) {
        this.whitePostService = whitePostService;
    }

    @GetMapping("/white/post")
    public ResponseEntity<PagedResponse<WhitePostDto>> getPosts(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String userId) {
        List<WhitePostDto> dtos = whitePostService.getPosts(sort, cursor, size, userId);

        String nextCursor = whitePostService.getNextCursor(dtos, sort);

        PagedResponse<WhitePostDto> response = new PagedResponse<>();
        response.setData(dtos);
        response.setNextCursor(nextCursor);

        return ResponseEntity.ok(response);
    }

}
