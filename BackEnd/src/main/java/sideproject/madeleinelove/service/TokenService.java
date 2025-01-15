package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.TokenDTO;

import java.util.UUID;

@Service
public interface TokenService {
    String reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
    ObjectId getUserIdFromAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken);
}