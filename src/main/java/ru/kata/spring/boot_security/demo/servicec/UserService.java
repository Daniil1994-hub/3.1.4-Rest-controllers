// UserService.java (дополнен)
package ru.kata.spring.boot_security.demo.servicec;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User findByUsername(String username);

    // Новые методы для работы с DTO
    List<UserDTO> getAllUsersDTO();
    UserDTO getUserDTOById(Long id);
    UserDTO saveUserDTO(UserDTO userDTO);
    UserDTO updateUserDTO(Long id, UserDTO userDTO);
}