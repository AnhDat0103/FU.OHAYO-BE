package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminSearchUserResponse {
    private int userId;
    private String fullName;
    private String email;
    private UserStatus status;
    private Date createdAt;
    private Date updatedAt;
    private Provider provider;
    private MembershipLevel membershipLevel;
}

