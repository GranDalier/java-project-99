package hexlet.code.app.component;

import hexlet.code.app.model.User;

import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        var userData = createAdmin();

        userRepository.save(userData);
    }

    private User createAdmin() {
        var email = "hexlet@example.com";
        var user = new User();
        user.setEmail(email);
        user.setFirstName("Tota");
        user.setLastName("Admin");
        user.setRole("ADMIN");
        var passwordDigest = passwordEncoder.encode("qwerty");
        user.setPasswordDigest(passwordDigest);
        return user;
    }
}
