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
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.validation.PasswordMatchConstant;

@PasswordMatchConstant(
        field = "password",
        fieldMatch = "confirmPassword",
        message = "Password and Confirm Password do not match"
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRegister {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Email
    private String email;

    @NotNull
    @Size(min = 5)
    private String password;

    @NotNull
    @Size(min = 5)
    private String confirmPassword;

    @NotNull
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0[0-9]{9,10}$")
    private String phone;

    private String address;

}
