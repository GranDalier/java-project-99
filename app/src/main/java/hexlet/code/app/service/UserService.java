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
    private UserMapper userMapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO userData) {
        User user = userMapper.map(userData);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO update(long id, UserUpdateDTO userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        userMapper.update(userData, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        userRepository.delete(user);
    }
}
