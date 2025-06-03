package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

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
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private LessonExercise lessonExercise;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private ContentListening contentListening;

    @OneToMany(mappedBy = "exerciseQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswerQuestion> answerQuestions;

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
