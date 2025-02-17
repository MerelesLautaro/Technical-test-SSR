package com.lautadev.technical_test.DTO.request.customer;

import jakarta.validation.constraints.NotBlank;

public record UserEditRequest(@NotBlank(message = "name not can't blank") String name) {
}
