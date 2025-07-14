package vn.fu_ohayo.mapper;

import org.mapstruct.*;
import vn.fu_ohayo.dto.request.*;
import vn.fu_ohayo.dto.request.Admin.User.AdminCreateUserRequest;
import vn.fu_ohayo.dto.request.Admin.User.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.response.Admin.User.AdminCheckEmailUserResponse;
import vn.fu_ohayo.dto.response.AdminDTO;
import vn.fu_ohayo.dto.response.Admin.User.AdminFilterUserResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "user", source = "student")
    List<StudentDTO> toStudentOnlyDtoList(List<ParentStudent> psList);

    @Mapping(target = "user", source = "parent")
    List<ParentStudentDTO> toParentOnlyDtoList(List<ParentStudent> psList);

    @Mapping(source = "role.name", target = "roleName")
    UserResponse toUserResponse(User user);

    @Mapping(source = "roles", target = "roles")
    AdminDTO toAdmin(Admin admin);

    User toUser(AdminCreateUserRequest addUserRequest);

    AdminFilterUserResponse toAdminFilterUserResponse(User user);

    void updateUserFromAdminRequest(@MappingTarget User user, AdminUpdateUserRequest request);

    AdminCheckEmailUserResponse toAdminCheckEmailUserResponseWithoutExist(User user);
    default AdminCheckEmailUserResponse toAdminCheckEmailUserResponse(User user) {
        AdminCheckEmailUserResponse res = toAdminCheckEmailUserResponseWithoutExist(user);
        res.setEmailExists(true);
        res.setDeleted(user.isDeleted());
        return res;
    }
}
