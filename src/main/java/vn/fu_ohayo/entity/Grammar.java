package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Grammars",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title_jp", "lesson_id"})
        },
        indexes = {
                @Index(columnList = "title_jp", name = "title_jp_index"),
                @Index(columnList = "jlpt_level", name = "jlpt_level_index")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Grammar {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "grammar_id")
    private int grammarId;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_TITLE)
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    @Column(name = "title_jp")
    private String titleJp;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_STRUCTURE)
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String structure;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_MEANING)
    @Size(max = 200, message = ErrorEnum.MAX_LENGTH_200)
    private String meaning;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    @Column(name = "usage_description")
    private String usage;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String example;

    @NotNull(message = ErrorEnum.NOT_EMPTY_JLPT_LEVEL)
    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level")
    private JlptLevel jlptLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(mappedBy = "grammars", fetch = FetchType.LAZY)
    private Set<ContentReading> contentReadings;

    @ManyToMany(mappedBy = "grammars", fetch = FetchType.LAZY)
    private Set<FavoriteGrammar> favoriteGrammars;

    @Column(name = "is_deleted")
    private boolean deleted = false;

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