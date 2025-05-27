package vn.fu_ohayo.entity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;

@Entity
@Table(name = "Contents")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Content {
    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_id")
    private long contentId;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CONTENT_TYPE)
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType;

//    @OneToOne(mappedBy = "content")
//    private ContentListening contentListening;
//
//    @OneToOne(mappedBy = "content")
//    private ContentReading ContentReading;
//
//    @OneToOne(mappedBy = "content")
//    private ContentSpeaking contentSpeaking;

}
