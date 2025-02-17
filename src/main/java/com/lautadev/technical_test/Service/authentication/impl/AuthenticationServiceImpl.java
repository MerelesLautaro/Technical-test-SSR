package com.lautadev.technical_test.Service.authentication.impl;

import com.lautadev.technical_test.DTO.request.authentication.UserRegisterRequest;
import com.lautadev.technical_test.DTO.response.authentication.Token;
import com.lautadev.technical_test.Entities.User;
import com.lautadev.technical_test.Repository.UserRepository;
import com.lautadev.technical_test.Service.authentication.AuthenticationService;
import com.lautadev.technical_test.Service.authentication.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    @Transactional
    public Token registerUser(UserRegisterRequest userRegisterRequest) {
        validateEmailUnique(userRegisterRequest);

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.password());

        User user = createUser(userRegisterRequest, encodedPassword);

        String token = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        return new Token(token,refreshToken);
    }

    public User createUser(UserRegisterRequest userRegisterRequest, String encodedPassword){
        User user = User.builder()
                .name(userRegisterRequest.name())
                .email(userRegisterRequest.email())
                .password(encodedPassword)
                .isDeleted(false)
                .build();

        user = userRepository.save(user);
        return user;
    }

    private void validateEmailUnique(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmailIgnoreCase(userRegisterRequest.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
