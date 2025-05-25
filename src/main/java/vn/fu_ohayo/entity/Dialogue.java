package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Dialogues")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Dialogue {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "dialogue_id")
    private long dialogueId;

    @ManyToOne
    @JoinColumn(name = "content_speaking_id")
    private ContentSpeaking contentSpeaking;

    @Min(value = 0, message = "sequence start from 0")
    private int sequence;

    @Column(name = "questiont_jp")
    private String questiontJp;

    @Column(name = "questiont_vn")
    private String questiontVn;

    @Column(name = "answer_vn")
    private String answerVn;

    @Column(name = "answer_jp")
    private String answerJp;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

}
