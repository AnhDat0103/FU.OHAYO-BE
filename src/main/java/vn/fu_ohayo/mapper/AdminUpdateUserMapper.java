package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.SearchUserRequest;
import vn.fu_ohayo.dto.response.SearchUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.User;

@Mapper(componentModel = "spring")
public interface AdminUpdateUserMapper {
    User toUser(AdminUpdateUserRequest adminUpdateUserRequest);
    UserResponse toUserResponse(User user);
}
