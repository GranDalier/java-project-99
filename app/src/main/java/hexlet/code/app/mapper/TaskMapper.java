package hexlet.code.app.mapper;

import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "modelToLabelId")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusToModel")
    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "labelIdToModel")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "statusToModel")
    @Mapping(source = "labelIds", target = "labels", qualifiedByName = "labelIdToModel")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("statusToModel")
    public final TaskStatus statusToModel(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow();
    }

    @Named("labelIdToModel")
    public final Set<Label> labelIdsToModel(Set<Long> labelIds) {
        return new HashSet<>(labelRepository.findByIdIn(labelIds));
    }

    @Named("modelToLabelId")
    public final Set<Long> modelToLabelIds(Set<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }
}
