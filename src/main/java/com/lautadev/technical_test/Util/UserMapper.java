package com.lautadev.technical_test.Util;

import com.lautadev.technical_test.DTO.request.customer.UserEditRequest;
import com.lautadev.technical_test.DTO.response.customer.UserDetailsResponse;
import com.lautadev.technical_test.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface  UserMapper {
    User userEditRequestToUser(UserEditRequest userEditRequest, @MappingTarget User user);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.deleted", target = "isDeleted")
    UserDetailsResponse userToUserDetailsResponse(User user);
}


