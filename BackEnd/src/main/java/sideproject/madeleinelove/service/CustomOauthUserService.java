package sideproject.madeleinelove.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import sideproject.madeleinelove.auth.CustomOauth2UserDetails;
import sideproject.madeleinelove.auth.KakaoUserDetails;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.entity.UserRole;
import sideproject.madeleinelove.repository.UserRepository;
import sideproject.madeleinelove.auth.NaverUserDetails;
import sideproject.madeleinelove.auth.OAuth2UserInfo;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

        switch (provider) {
            case "naver":
                log.info("네이버 로그인");
                oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
                break;
            case "kakao":
                log.info("카카오 로그인");
                oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
                break;
            default:
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider", "지원하지 않는 로그인 제공자입니다.", null));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId; // loginId 생성

        User findUser = userRepository.findByProviderId(providerId);
        User user;

        if (findUser == null) {
            user = User.builder()
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else {
            user = findUser;
        }

        return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
    }
}