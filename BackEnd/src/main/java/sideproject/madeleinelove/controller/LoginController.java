package sideproject.madeleinelove.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sideproject.madeleinelove.auth.KakaoUserDetails;
import sideproject.madeleinelove.auth.NaverUserDetails;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.service.UserService;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<String> loginSuccess(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();
        String provider = authentication.getAuthorizedClientRegistrationId();
        String email;
        String providerId;

        if ("naver".equals(provider)) {
            NaverUserDetails naverUserDetails = new NaverUserDetails(oauth2User.getAttributes());
            email = naverUserDetails.getEmail();
            providerId = naverUserDetails.getProviderId();
        }
        else if ("kakao".equals(provider)) {
            KakaoUserDetails kakaoUserDetails = new KakaoUserDetails(oauth2User.getAttributes());
            email = kakaoUserDetails.getEmail();
            providerId = kakaoUserDetails.getProviderId();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("지원하지 않는 로그인 제공자입니다.");
        }

        User user = userService.getUserByProviderId(providerId);
        if (user == null) {
            user = userService.createUser(email, provider, providerId);
        }

        return ResponseEntity.ok("로그인 성공: " + providerId);

    }
}