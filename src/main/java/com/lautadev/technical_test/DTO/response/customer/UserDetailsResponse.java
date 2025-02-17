package com.lautadev.technical_test.DTO.response.customer;


public record UserDetailsResponse(Long id,
                                  String name,
                                  String email,
                                  boolean isDeleted) {
}
