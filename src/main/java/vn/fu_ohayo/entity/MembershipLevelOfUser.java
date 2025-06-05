package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "membership_level_of_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipLevelOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
     User user;

    @ManyToOne
    @JoinColumn(name = "membership_level_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
     MembershipLevel membershipLevel;

    @Column(nullable = false)
     LocalDate startDate;

    @Column(nullable = false)
     LocalDate endDate;

    // ✅ Tự gán startDate nếu chưa được gán
    @PrePersist
    public void prePersist() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
    }
}
