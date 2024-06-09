package hexlet.code.app.service;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public UserDTO findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        return mapper.map(user);
    }

    public UserDTO create(UserCreateDTO userData) {
        User user = mapper.map(userData);
        user.setRole("USER");
        userRepository.save(user);
        return mapper.map(user);
    }

    public UserDTO update(long id, UserUpdateDTO userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        mapper.update(userData, user);
        userRepository.save(user);
        return mapper.map(user);
    }

    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        userRepository.delete(user);
    }
}
