package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.AdminSearchUserRequest;
import vn.fu_ohayo.dto.response.AdminSearchUserResponse;
import vn.fu_ohayo.entity.User;

@Mapper(componentModel = "spring")
public interface SearchUserMapper {
    User toUser(AdminSearchUserRequest adminSearchUserRequest);
    AdminSearchUserResponse toSearchUserResponse(User user);
}
