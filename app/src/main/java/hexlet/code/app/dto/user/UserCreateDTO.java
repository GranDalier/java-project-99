package hexlet.code.app.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    @Email(regexp = "^\\w{3,}(\\.\\w{3,})*@(\\w+\\.){1}\\w{2,4}$")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;
}
