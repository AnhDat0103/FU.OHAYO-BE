package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Admins")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Admin {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int adminId;

    private String username;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "Admin_Roles",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
