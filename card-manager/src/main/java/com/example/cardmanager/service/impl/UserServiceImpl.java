package com.example.cardmanager.service.impl;

import com.example.cardmanager.dto.UserDto;
import com.example.cardmanager.entity.Role;
import com.example.cardmanager.entity.User;
import com.example.cardmanager.repository.RoleRepository;
import com.example.cardmanager.repository.UserRepository;
import com.example.cardmanager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private String specialemail = "admin@cardmanager.it";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatoutente("Attivo");

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = setRoleAdmin();
            role = setRoleUser();
            role = setRoleSeller();
        }
        if(user.getEmail().equals(specialemail)) {
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN")));
        } 
        else if(user.getEmail().contains("@seller")){
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_SELLER")));
        }        
        else {
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        }
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        
        return userDto;
    }

    private Role setRoleAdmin() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    private Role setRoleUser() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    private Role setRoleSeller(){
        Role role = new Role();
        role.setName("ROLE_SELLER");
        return roleRepository.save(role);
    }

    public void blockUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setStatoutente("Bloccato");
        userRepository.save(user);
    }

    public void activeUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setStatoutente("Attivo");
        userRepository.save(user);
    }

}