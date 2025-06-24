package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardTotalUserEachWeekResponse {
    int totalUserWeek1;
    int totalUserWeek2;
    int totalUserWeek3;
    int totalUserWeek4;
}
