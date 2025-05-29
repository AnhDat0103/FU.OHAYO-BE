package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryListeningEnum;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Date;

@Entity
@Table(name = "Content_Listenings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentListening {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_listening_id")
    private long contentListeningId;

    //practiceID
//    @OneToMany(mappedBy = "Exercise")
//    private Exercise exercise;

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

    private String url;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @Column(name = "script_jp")
    private String scriptJp;

    @Column(name = "script_vn")
    private String script_vn;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    @Enumerated(EnumType.STRING)
    private CategoryListeningEnum category;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
