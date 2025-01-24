package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.TokenDTO;

import java.util.Date;

@Service
public interface TokenService {
    String reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
    TokenDTO.TokenResponse validateAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
    ObjectId getUserIdFromAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
    void addToBlacklist(String token, Date expirationDate);
    boolean isTokenBlacklisted(String token);
}