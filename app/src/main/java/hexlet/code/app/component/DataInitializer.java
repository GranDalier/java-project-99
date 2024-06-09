package hexlet.code.app.component;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;

import hexlet.code.app.repository.TaskStatusRepository;
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
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
        createTaskStatus("Draft", "draft");
        createTaskStatus("ToReview", "to_review");
        createTaskStatus("ToBeFixed", "to_be_fixed");
        createTaskStatus("ToPublish", "to_publish");
        createTaskStatus("Published", "published");
    }

    private void createAdmin() {
        var email = "hexlet@example.com";
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        var user = new User();
        user.setEmail(email);
        user.setFirstName("Tota");
        user.setLastName("Admin");
        user.setRole("ADMIN");
        var passwordDigest = passwordEncoder.encode("qwerty");
        user.setPasswordDigest(passwordDigest);

        userRepository.save(user);
    }

    private void createTaskStatus(String name, String slug) {
        if (taskStatusRepository.findBySlug(slug).isPresent()) {
            return;
        }
        var taskStatus = new TaskStatus();
        taskStatus.setName(name);
        taskStatus.setSlug(slug);
        taskStatusRepository.save(taskStatus);
    }
}
