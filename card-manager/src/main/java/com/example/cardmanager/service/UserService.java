package com.example.cardmanager.service;

import com.example.cardmanager.dto.UserDto;
import com.example.cardmanager.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void blockUser(String email);

    void activeUser(String email);
}