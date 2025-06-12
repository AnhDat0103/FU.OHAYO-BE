package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentStudentDTO {
    private Long id;
    private SimpleUserDTO user;
}