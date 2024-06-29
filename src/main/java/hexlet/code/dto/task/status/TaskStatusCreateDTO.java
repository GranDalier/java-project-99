package hexlet.code.dto.task.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z]+(?:_[a-z]+)*$")
    private String slug;
}
