package com.behl.ehrmantraut.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.entity.User;
import com.behl.ehrmantraut.exception.DuplicateEmailIdException;
import com.behl.ehrmantraut.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(final UserCreationRequestDto userCreationRequestDto) {
        if (!isEmailIdUnique(userCreationRequestDto.getEmailId()))
            throw new DuplicateEmailIdException();

        final var user = new User();
        user.setFullName(userCreationRequestDto.getFullName());
        user.setEmailId(userCreationRequestDto.getEmailId());
        user.setPassword(passwordEncoder.encode(userCreationRequestDto.getPassword()));
        userRepository.save(user);
    }

    private boolean isEmailIdUnique(final String emailId) {
        return !userRepository.existsByEmailId(emailId);
    }

}
