package hexlet.code.app.component;

import hexlet.code.app.model.User;

import hexlet.code.app.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class DataInitializer implements ApplicationRunner {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void run(ApplicationArguments args) {
        var userData = createAdmin();
        userData.setRole("ADMIN");
        userDetailsService.createUser(userData);
    }

    private User createAdmin() {
        var email = "hexlet@example.com";
        var user = new User();
        user.setEmail(email);
        user.setFirstName("Tota");
        user.setLastName("Admin");
        user.setPasswordDigest("qwerty");
        return user;
    }
}
