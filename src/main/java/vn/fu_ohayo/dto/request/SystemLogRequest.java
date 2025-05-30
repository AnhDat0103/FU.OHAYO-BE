package vn.fu_ohayo.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.RoleEnum;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemLogRequest {
    LocalDateTime startTimestamp;
    LocalDateTime endTimestamp;
    String action;
    String details;
    RoleEnum role;
}
