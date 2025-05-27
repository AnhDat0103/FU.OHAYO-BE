package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import vn.fu_ohayo.enums.ErrorEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentSpeakingRequest {
    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;
    @NotNull(message = ErrorEnum.NOT_EMPTY_IMAGE)
    private String image;
    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    private CategorySpeakingEnum category;
}
