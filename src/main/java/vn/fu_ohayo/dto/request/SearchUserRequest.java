package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.RoleEnum;
import vn.fu_ohayo.enums.UserStatus;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserRequest {
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    MembershipLevel membershipLevel;

    UserStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredTo;
}
