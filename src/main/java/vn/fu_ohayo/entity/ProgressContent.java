package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Content_Progress")
public class ProgressContent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "score")
    @Size(max = 10)
    private int score;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
