package vn.fu_ohayo.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.ErrorEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdminUpdateUserRequest {

    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = ErrorEnum.INVALID_EMAIL_MS
    )
    @Email(message = ErrorEnum.INVALID_EMAIL_MS)
    String email;

    @Pattern(
            regexp = "^(ACTIVE|INACTIVE|BANNED)?$",
            message = ErrorEnum.INVALID_STATUS_MS
    )
    String status;

    @Pattern(
            regexp = "^(NORMAL|VIP)?$",
            message = ErrorEnum.INVALID_MEMBERSHIP_MS
    )
    String membershipLevel;


}
