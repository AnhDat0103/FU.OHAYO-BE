package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Lessons",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "lesson_name")
},
        indexes = {
                @Index(name = "idx_lesson_id", columnList = "lesson_id"),
                @Index(name = "idx_lesson_name", columnList = "lesson_name"),
                @Index(name = "idx_subject_id", columnList = "subject_id")
}
        )
public class Lesson {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "lesson_id")
    private int lessonId;

    @Column(name = "lesson_name", nullable = false)
    private String name;

    @Column(name = "lesson_description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

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
