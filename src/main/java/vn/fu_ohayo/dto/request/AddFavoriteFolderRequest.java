package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddFavoriteFolderRequest {

    @NotBlank(message = "Folder name cannot be empty")
    @Size(max = 100, message = "Folder name must be at most 100 characters")
    String name;

    @NotNull(message = "Visibility (isPublic) must be specified")
    Boolean isPublic;

    @NotBlank(message = "Type must be specified")
    @Pattern(
            regexp = "^(vocabulary|grammar)$",
            message = "Type must be either 'vocabulary' or 'grammar'"
    )
    String type;
}
