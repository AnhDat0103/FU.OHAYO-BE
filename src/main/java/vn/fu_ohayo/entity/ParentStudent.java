package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Relationship;

@Entity
@Table(name = "Parent_Students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParentStudent {
    @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "user_id")
    private User parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @Column(name = "relationship")
    @Enumerated(EnumType.STRING)
    private Relationship relationship;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "verification_code")
    private String verificationCode;

}
