package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.CategorySpeakingEnum;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentReadingResponse {
    private long contentReadingId;
    private String title;
    private String image;
    private CategoryReadingEnum category;
    private Date createdAt;
    private Date updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timeNew;
    private String scriptJp;
    private String scriptVn;
    private String audioFile;

}
