package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long userId;

    private String email;

    private String fullName;

    private Gender gender;

    private String phone;

    private String address;

    private MembershipLevel membershipLevel;

    private UserStatus status;

}
