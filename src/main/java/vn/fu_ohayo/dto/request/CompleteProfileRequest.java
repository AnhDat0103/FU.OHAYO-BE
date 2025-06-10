package vn.fu_ohayo.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class CompleteProfileRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private Gender gender; // hoặc Enum

    @NotNull
    private Date dob;

    @NotNull
    private RoleEnum role;

    private String address;

    @NotBlank
    private String phone;
}
