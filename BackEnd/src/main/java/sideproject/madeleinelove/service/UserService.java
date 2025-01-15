package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepository userRepository;

    public ObjectId validateUser(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) {

        String accessToken = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        ObjectId userId = tokenServiceImpl.getUserIdFromAccessToken(request, response, accessToken);
        return userId;
    }
}
