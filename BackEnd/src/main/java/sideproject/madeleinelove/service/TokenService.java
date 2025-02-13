package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import sideproject.madeleinelove.dto.TokenDTO;

import java.util.Date;

public interface TokenService {
    String reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
    TokenDTO.TokenResponse validateAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
    ObjectId getUserIdFromAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
    void addToBlacklist(String token, Date expirationDate);
    boolean isTokenBlacklisted(String token);
}