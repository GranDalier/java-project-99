package hexlet.code.app.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class UserUpdateDTO {

    @Email(regexp = "^\\w{3,}(\\.\\w{3,})*@(\\w+\\.){1}\\w{2,4}$")
    private JsonNullable<String> email;

    @NotBlank
    private JsonNullable<String> firstName;

    @NotBlank
    private JsonNullable<String> lastName;

    @NotBlank
    private JsonNullable<String> password;
}
