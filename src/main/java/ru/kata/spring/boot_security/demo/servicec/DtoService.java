package ru.kata.spring.boot_security.demo.servicec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DtoService {

    private final RoleService roleService;

    @Autowired
    public DtoService(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleService::findByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }
}