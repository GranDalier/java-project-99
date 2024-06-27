package hexlet.code.app.mapper;

import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusToModel")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "statusToModel")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("statusToModel")
    public final TaskStatus statusToModel(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow();
    }
}
