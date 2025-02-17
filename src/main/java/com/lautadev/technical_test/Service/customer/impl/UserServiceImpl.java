package com.lautadev.technical_test.Service.customer.impl;

import com.lautadev.technical_test.DTO.request.customer.UserEditRequest;
import com.lautadev.technical_test.DTO.response.customer.UserDetailsResponse;
import com.lautadev.technical_test.Entities.User;
import com.lautadev.technical_test.Exception.UserNotFoundException;
import com.lautadev.technical_test.Repository.UserRepository;
import com.lautadev.technical_test.Repository.specifications.UserSpecification;
import com.lautadev.technical_test.Service.customer.UserService;
import com.lautadev.technical_test.Util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::userNotFound);
    }

    @Override
    @Cacheable(value = "users", key = "#filters.toString() + #pageable.pageNumber + #pageable.pageSize")
    public Page<UserDetailsResponse> getUsers(Map<String, String> filters, Pageable pageable) {
        Specification<User> spec = new UserSpecification(filters);

        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<UserDetailsResponse> userDetailsResponseList = userPage.stream()
                .map(userMapper::userToUserDetailsResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(userDetailsResponseList, pageable, userPage.getTotalElements());
    }

    @Override
    public UserDetailsResponse findUserById(Long id) {
        User user = userRepository.findByIdAndIsNotDeleted(id).orElseThrow(UserNotFoundException::userNotFound);
        return userMapper.userToUserDetailsResponse(user);
    }

    @Override
    @Transactional
    public UserDetailsResponse editUser(Long id, UserEditRequest userEditRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::userNotFound);
        User userEdit = userMapper.userEditRequestToUser(userEditRequest,user);
        userRepository.save(userEdit);
        return userMapper.userToUserDetailsResponse(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::userNotFound);
        user.setDeleted(true);
        userRepository.save(user);
    }

}