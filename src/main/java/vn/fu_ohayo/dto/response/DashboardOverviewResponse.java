package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.SystemLog;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardOverviewResponse {
    int totalUserNow;
    int totalUserLastMonth;
    int totalSubjectNow;
    int totalSubjectLastMonth;
    int totalLessonNow;
    int totalLessonLastMonth;
    double completionRateSubjectNow;
    double completionRateSubjectLastMonth;
    int totalUserNormal;
    int totalUserOneYear;
    int totalUserOneMonth;
    int totalUserSixMonths;
    List<SystemLog> systemLogs;
}
