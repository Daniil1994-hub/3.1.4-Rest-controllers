package ru.kata.spring.boot_security.demo.servicec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoService dtoService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           DtoService dtoService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoService = dtoService;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        // Используем метод, который загружает роли
        return userRepository.findAllWithRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);

        // Обновляем только если пароль изменился
        if (!user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRoles(user.getRoles());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsersDTO() {
        return userRepository.findAllWithRoles().stream()
                .map(dtoService::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserDTOById(Long id) {
        User user = getUserById(id);
        return dtoService.convertToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO saveUserDTO(UserDTO userDTO) {
        User user = dtoService.convertToUser(userDTO);
        User savedUser = saveUser(user);
        return dtoService.convertToUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUserDTO(Long id, UserDTO userDTO) {
        User user = dtoService.convertToUser(userDTO);
        User updatedUser = updateUser(id, user);
        return dtoService.convertToUserDTO(updatedUser);
    }
}