package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Content_Speakings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentSpeaking {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_speaking_id")
    private int contentSpeakingId;

    @OneToMany(mappedBy = "contentSpeaking")
    private List<Dialogue> Dialogues;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;


    @OneToOne(cascade = CascadeType.ALL)
    //khi lưu 1 đối tượng ContentSpeaking , nếy chưa có content thì tự động lưu đối tượng content rồi mới lưu ContentSpeaking
    // khi xóa ContentSpeaking thì tự động xóa content
    @JoinColumn(name = "content_id")
    private Content content;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    @Enumerated(EnumType.STRING)
    private CategorySpeakingEnum category;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }


}
