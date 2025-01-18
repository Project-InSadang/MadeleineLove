package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.auth.JwtUtil;
import sideproject.madeleinelove.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenServiceImpl tokenServiceImpl;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ObjectId getUserIdFromAccessToken(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        String userId = jwtUtil.getUserIdFromToken(accessToken);
        return new ObjectId(userId);
    }
}
