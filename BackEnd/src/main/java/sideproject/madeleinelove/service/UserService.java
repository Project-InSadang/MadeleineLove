package sideproject.madeleinelove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.UserRepository;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.entity.UserRole;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String email, String provider, String providerId) {
        User user = User.builder()
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .role(UserRole.USER)
                .build();
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByProviderId(String providerId) { return userRepository.findByProviderId(providerId); }
}