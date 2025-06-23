package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSearchUserRequest {
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    MembershipLevel membershipLevel;

    UserStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredTo;

    private Integer currentPage = 0;
    private Integer pageSize = 10;
}
