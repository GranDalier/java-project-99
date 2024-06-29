package hexlet.code.dto.task.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.openapitools.jackson.nullable.JsonNullable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusUpdateDTO {

    @NotBlank
    private JsonNullable<String> name;

    @NotBlank
    @Pattern(regexp = "^[a-z]+(?:_[a-z]+)*$")
    private JsonNullable<String> slug;
}
