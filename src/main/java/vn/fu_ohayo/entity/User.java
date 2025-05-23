package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;


@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        },
        indexes = {
                @Index(columnList = "email", unique = true,
                        name = "email_index"),
                @Index(columnList = "phone", unique = true,
                        name = "phone_index"),
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    @Id @GeneratedValue(
             strategy = GenerationType.IDENTITY
    )
    @Column(name = "user_id")
    private int userId;

    @Email
    @Column(unique = true)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotNull
    @Size(min = 5)
    private String password;

    @NotNull
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0[0-9]{9,10}$")
    @Column(unique = true)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Size(max = 255)
    private String avatar;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
