package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Exercise_Questions")
public class ExerciseQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_question_id")
    private int exerciseQuestionId;

    @Column(name = "question_text", columnDefinition = "TEXT")
    @NotNull(message = "Question text cannot be null")
    @NotBlank(message = "Question text cannot be blank")
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = true)
    private LessonExercise lessonExercise;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = true)
    private ContentListening contentListening;

    @OneToMany(mappedBy = "exerciseQuestion", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
//    @Size(min = 2, message = "The list must contain at least 2 answer")
    private List<AnswerQuestion> answerQuestions;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

}
