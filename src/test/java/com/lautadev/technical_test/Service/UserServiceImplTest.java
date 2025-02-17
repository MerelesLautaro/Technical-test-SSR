package com.lautadev.technical_test.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.lautadev.technical_test.DTO.request.customer.UserEditRequest;
import com.lautadev.technical_test.DTO.response.customer.UserDetailsResponse;
import com.lautadev.technical_test.Entities.User;
import com.lautadev.technical_test.Exception.UserNotFoundException;
import com.lautadev.technical_test.Repository.UserRepository;
import com.lautadev.technical_test.Service.customer.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.*;
import java.util.Optional;




/**
 * The Lombok annotations generate errors in the tests. To make them work,
 * they must be added manually. It seems to be a version incompatibility issue.
 */

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Pageable pageable;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testLoadUserByUsername_throwsUsernameNotFoundException() {
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void testLoadUserByUsername_success() {
        String username = "user@example.com";
        User user = new User(1L, "User", "password", username, false);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetUsers() {
        Map<String, String> filters = new HashMap<>();
        Page<User> userPage = new PageImpl<>(List.of(new User(1L, "User", "password", "user@example.com", false)));
        when(userRepository.findAll((Example<User>) any(), eq(pageable))).thenReturn(userPage);

        Page<UserDetailsResponse> result = userService.getUsers(filters, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
    @Test
    void testFindUserById_userNotFound() {
        Long userId = 1L;
        when(userRepository.findByIdAndIsNotDeleted(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void testFindUserById_success() {
        Long userId = 1L;
        User user = new User(userId, "User", "password", "user@example.com", false);
        when(userRepository.findByIdAndIsNotDeleted(userId)).thenReturn(Optional.of(user));

        UserDetailsResponse result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
    }

    @Test
    void testEditUser_userNotFound() {
        Long userId = 1L;
        UserEditRequest userEditRequest = new UserEditRequest("Updated User");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.editUser(userId, userEditRequest));
    }

    @Test
    void testEditUser_success() {
        Long userId = 1L;
        User existingUser = new User(userId, "User", "password", "user@example.com", false);
        UserEditRequest userEditRequest = new UserEditRequest("Updated User");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDetailsResponse result = userService.editUser(userId, userEditRequest);

        assertNotNull(result);
        assertEquals("Updated User", result.name());
    }

    @Test
    void testDeleteUserById_userNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
    }

    @Test
    void testDeleteUserById_success() {
        Long userId = 1L;
        User user = new User(userId, "User", "password", "user@example.com", false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deleteUserById(userId);

        verify(userRepository).save(any(User.class));
        assertTrue(user.isDeleted());
    }

}
