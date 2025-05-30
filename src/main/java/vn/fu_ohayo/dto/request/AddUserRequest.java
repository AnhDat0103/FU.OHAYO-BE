package vn.fu_ohayo.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddUserRequest {
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = ErrorEnum.INVALID_EMAIL_MS
    )
    @Email(message = ErrorEnum.INVALID_EMAIL_MS)
    String email;

    @NotNull(message = ErrorEnum.NOT_EMPTY_NAME)
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    private String username;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PASSWORD)
    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String password;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    @Column(unique = true)
    private String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
