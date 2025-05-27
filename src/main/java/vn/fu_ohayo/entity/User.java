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
import vn.fu_ohayo.enums.*;

import java.util.Date;
import java.util.List;
import java.util.Set;


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
    @NotNull(message = ErrorEnum.NOT_EMPTY_EMAIL)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ErrorEnum.INVALID_EMAIL_MS)
    private String email;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PASSWORD)
    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String password;

    @NotNull(message = ErrorEnum.NOT_EMPTY_NAME)
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    @Column(unique = true)
    private String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String avatar;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany
    @JoinTable(
            name = "User_Subjects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "parent")
    private List<ParentStudent> children;

    @OneToMany(mappedBy = "student")
    private List<ParentStudent> parents;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
