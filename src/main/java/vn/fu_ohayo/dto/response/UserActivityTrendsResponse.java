package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserActivityTrendsResponse {
    int totalUserActiveWeek1;
    int totalUserActiveWeek2;
    int totalUserActiveWeek3;
    int totalUserActiveWeek4;
}
