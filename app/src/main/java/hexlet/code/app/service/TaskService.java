package hexlet.code.app.service;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public TaskDTO findById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task id " + id + " not found"));
        return mapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        Task task = mapper.map(taskData);
        taskRepository.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(long id, TaskUpdateDTO taskData) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task id " + id + " not found"));
        mapper.update(taskData, task);
        taskRepository.save(task);
        return mapper.map(task);
    }

    public void delete(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task id " + id + " not found"));
        taskRepository.delete(task);
    }
}
