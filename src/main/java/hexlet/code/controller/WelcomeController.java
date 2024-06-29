package hexlet.code.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/welcome")
public final class WelcomeController {

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public String welcome() {
        return "Welcome to Spring";
    }

}
