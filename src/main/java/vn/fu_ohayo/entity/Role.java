package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Set;

@Entity
@Table(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Role {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Admin> admins;

    @ManyToMany()
    @JoinTable(
            name = "Role_Permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
