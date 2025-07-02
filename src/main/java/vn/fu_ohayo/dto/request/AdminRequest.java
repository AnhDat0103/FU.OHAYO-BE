package vn.fu_ohayo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRequest {
    String username;
    String password;
    Set<Integer> roleIds;
}
