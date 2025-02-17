package com.lautadev.technical_test.Controller.authentication;

import com.lautadev.technical_test.DTO.request.authentication.UserRegisterRequest;
import com.lautadev.technical_test.DTO.response.authentication.Token;
import com.lautadev.technical_test.Service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/users")
    public ResponseEntity<Token> registerUser(@RequestBody @Valid
                                                  UserRegisterRequest userRegisterRequest){
        return ResponseEntity.ok(authenticationService.registerUser(userRegisterRequest));
    }
}
