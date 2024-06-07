package hexlet.code.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;

import java.util.Optional;

@Component
public final class UserUtils {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
