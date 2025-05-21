package vn.fu_ohayo.enums;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.SimpleTriggerContext;

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

    @Column(name = "parent_id")
    @NotNull
    private String parentId;

    @Column(name = "student_id")
    @NotNull
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship")
    private String relationship;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "verification_code")
    private String verificationCode;

}
