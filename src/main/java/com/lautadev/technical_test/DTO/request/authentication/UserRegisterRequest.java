package com.lautadev.technical_test.DTO.request.authentication;

import com.lautadev.technical_test.Util.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(@NotBlank String name,
                                  @NotBlank @Email String email,
                                  @Password String password) {
}
