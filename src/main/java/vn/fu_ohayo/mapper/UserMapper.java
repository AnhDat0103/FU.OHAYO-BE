package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRegister userRegister);
    @Mapping(source = "role.name", target = "roleName")
    UserResponse toUserResponse(User user);
    User toUser(AddUserRequest addUserRequest);
    User toUser(UserResponse userResponse);
}
