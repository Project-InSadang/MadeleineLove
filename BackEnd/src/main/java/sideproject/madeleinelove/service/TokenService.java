package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.TokenDTO;

@Service
public interface TokenService {
    String reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
    TokenDTO.TokenResponse validateAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
}