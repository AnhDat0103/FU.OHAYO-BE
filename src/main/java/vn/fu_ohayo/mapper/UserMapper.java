package vn.fu_ohayo.mapper;

import org.mapstruct.*;
import vn.fu_ohayo.dto.request.*;
import vn.fu_ohayo.dto.request.Admin.User.AdminCreateUserRequest;
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

    User toUser(UserRegister userRegister);
    @Mapping(target = "user", source = "student")
    StudentDTO toStudent(ParentStudent ps);

    @Mapping(target = "user", source = "parent")
    ParentStudentDTO toParent(ParentStudent ps);

    List<StudentDTO> toStudentOnlyDtoList(List<ParentStudent> psList);
    List<ParentStudentDTO> toParentOnlyDtoList(List<ParentStudent> psList);

    SimpleUserDTO toDto(User user);

    @Mapping(source = "role.name", target = "roleName")
    UserResponse toUserResponse(User user);

    @Mapping(source = "roles", target = "roles")
    AdminDTO toAdmin(Admin admin);

    User toUser(AdminCreateUserRequest addUserRequest);
    User toUser(UserResponse userResponse);

    AdminFilterUserResponse toAdminFilterUserResponse(User user);
    AdminCheckEmailUserResponse toAdminCheckEmailUserResponse(User user);}
