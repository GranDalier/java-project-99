package hexlet.code.service;

import hexlet.code.dto.task.status.TaskStatusDTO;
import hexlet.code.dto.task.status.TaskStatusCreateDTO;
import hexlet.code.dto.task.status.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceAlreadyExistsException;
import hexlet.code.model.TaskStatus;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> findAll() {
        return taskStatusRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public TaskStatusDTO findById(long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status id " + id + " not found"));
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        TaskStatus taskStatus = mapper.map(taskStatusData);
        checkIfUnique(taskStatus);
        taskStatusRepository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO update(long id, TaskStatusUpdateDTO taskStatusData) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status id " + id + " not found"));
        mapper.update(taskStatusData, taskStatus);
        checkIfUnique(taskStatus);
        taskStatusRepository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public void delete(long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status id " + id + " not found"));
        taskStatusRepository.delete(taskStatus);
    }

    private void checkIfUnique(TaskStatus taskStatus) {
        var name = taskStatus.getName();
        var slug = taskStatus.getSlug();
        var taskStatuses = taskStatusRepository.findAllByNameOrSlug(name, slug).stream()
                .filter(st -> !st.getId().equals(taskStatus.getId()))
                .toList();
        if (!taskStatuses.isEmpty()) {
            throw new ResourceAlreadyExistsException("Task status with such name or slug already exists");
        }
    }
}
