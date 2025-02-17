package com.lautadev.technical_test.Service.customer;

import com.lautadev.technical_test.DTO.request.customer.UserEditRequest;
import com.lautadev.technical_test.DTO.response.customer.UserDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    Page<UserDetailsResponse> getUsers(Map<String, String> filters, Pageable pageable);
    UserDetailsResponse findUserById(Long id);
    UserDetailsResponse editUser(Long id, UserEditRequest userEditRequest);
    void deleteUserById(Long id);
}
