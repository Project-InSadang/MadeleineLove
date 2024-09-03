package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    void reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
}