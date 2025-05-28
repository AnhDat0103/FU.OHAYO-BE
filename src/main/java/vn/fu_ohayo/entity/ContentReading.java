package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Content_Readings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentReading {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_reading_id")
    private int contentReadingId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    //khi lưu 1 đối tượng ContentSpeaking , nếy chưa có content thì tự động lưu đối tượng content rồi mới lưu ContentSpeaking
    // khi xóa ContentSpeaking thì tự động xóa content
    @JoinColumn(name = "content_id")
    private Content content;

    @NotNull
    @PastOrPresent(message = "Time can not over today")
    private Date timeNew;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String audioFile;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @Column(name = "script_jp")
    private String scriptJp;

    @Column(name = "script_vn")
    private String script_vn;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    @Enumerated(EnumType.STRING)
    private CategoryReadingEnum category;

    @ManyToMany
    @JoinTable(
            name = "ContentReading_Vocabulary",
            joinColumns = @JoinColumn(name = "content_reading_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private Set<Vocabulary> vocabularies;

    @ManyToMany
    @JoinTable(
            name = "ContentReading_Grammar",
            joinColumns = @JoinColumn(name = "content_reading_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id")
    )
    private Set<Grammar> grammars;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

}
