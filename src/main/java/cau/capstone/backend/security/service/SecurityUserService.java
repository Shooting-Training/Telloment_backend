package cau.capstone.backend.security.service;

import cau.capstone.backend.model.DTO.UserDto;

import java.util.Optional;

public interface SecurityUserService {
    Optional<UserDto> login(UserDto userDto);
}