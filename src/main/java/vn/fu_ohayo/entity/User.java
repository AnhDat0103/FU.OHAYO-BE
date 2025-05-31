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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.enums.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
                @UniqueConstraint(columnNames = {"email", "provider"})
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
public class User implements UserDetails, Serializable {
    @Id @GeneratedValue(
             strategy = GenerationType.IDENTITY
    )
    @Column(name = "user_id")
    private int userId;

    @Email
    @NotNull(message = ErrorEnum.NOT_EMPTY_EMAIL)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ErrorEnum.INVALID_EMAIL_MS)
    private String email;

    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String password;


    @Column(name = "full_name")
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    private String fullName;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    @Column(unique = true)
    private String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    private String address;

    private Date dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel = MembershipLevel.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider = Provider.LOCAL;

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }


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

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user roles/authorities here. Example:
        return List.of();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
