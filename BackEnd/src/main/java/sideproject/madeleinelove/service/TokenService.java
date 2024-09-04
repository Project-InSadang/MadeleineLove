package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sideproject.madeleinelove.dto.TokenDto;

public interface TokenService {

    TokenDto.TokenResponse reissueAccessToken(HttpServletRequest request, HttpServletResponse response);

}
