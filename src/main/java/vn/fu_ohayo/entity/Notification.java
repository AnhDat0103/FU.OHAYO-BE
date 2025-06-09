package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    @Column(name = "notification_id")
    private int notificationId;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void createAtNow() {
        this.createdAt = new Date();
    }

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

}
