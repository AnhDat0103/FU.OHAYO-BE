package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRegister userRegister);
    UserResponse toUserResponse(User user);
}
