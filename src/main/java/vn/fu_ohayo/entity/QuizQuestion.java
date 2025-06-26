package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    String question;

    @OneToOne
    @JoinColumn(name =  "vocabulary_id", unique = true)
    private Vocabulary vocabulary;
}
