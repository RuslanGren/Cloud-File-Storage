package com.example.cloudfilestorage.web.mappers;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.web.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto>{
}
