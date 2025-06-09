package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Difficulty;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ExerciseType;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "Lesson_Exercises")
public class LessonExercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private int exerciseId;

    @Column(name = "title")
    private String title;

    @Size(min = 1, message = ErrorEnum.MIN_TIME_1)
    @Column(name = "duration")
    private long duration;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false)
    private ExerciseType exerciseType;

    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "lessonExercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExerciseResult> exerciseResults;

    @PrePersist
    public void createAtNow() {
        this.createdAt = new Date();}
}
