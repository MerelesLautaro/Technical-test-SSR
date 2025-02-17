package com.lautadev.technical_test.Controller.customer;

import com.lautadev.technical_test.DTO.request.customer.UserEditRequest;
import com.lautadev.technical_test.DTO.response.GenericResponse;
import com.lautadev.technical_test.DTO.response.customer.UserDetailsResponse;
import com.lautadev.technical_test.Service.customer.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDetailsResponse>> getUsers(@RequestParam Map<String, String> filters,
                                                              Pageable pageable){
        return ResponseEntity.ok(userService.getUsers(filters,pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> findUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> editUser(@RequestBody @Valid UserEditRequest userEditRequest,
                                                        @PathVariable Long id){
        return ResponseEntity.ok(userService.editUser(id, userEditRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok(new GenericResponse("User deleted"));
    }
}
