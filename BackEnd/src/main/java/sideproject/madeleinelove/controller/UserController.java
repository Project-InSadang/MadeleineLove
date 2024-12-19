package sideproject.madeleinelove.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        userService.withdraw(authorizationHeader);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}