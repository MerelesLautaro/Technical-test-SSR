package com.lautadev.technical_test.Service.authentication;

import com.lautadev.technical_test.DTO.request.authentication.UserRegisterRequest;
import com.lautadev.technical_test.DTO.response.authentication.Token;

public interface AuthenticationService {
    Token registerUser(UserRegisterRequest userRegisterRequest);
}
