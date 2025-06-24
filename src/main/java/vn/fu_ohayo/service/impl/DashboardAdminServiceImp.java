package vn.fu_ohayo.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.DashboardCouseraRateResponse;
import vn.fu_ohayo.dto.response.DashboardOverviewResponse;
import vn.fu_ohayo.dto.response.DashboardTotalUserEachWeekResponse;
import vn.fu_ohayo.entity.SystemLog;
import vn.fu_ohayo.enums.*;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.DashboardAdminService;
import vn.fu_ohayo.service.UserService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class DashboardAdminServiceImp implements DashboardAdminService {
    private ProgressSubjectRepository progressSubjectRepository;
    private UserRepository userRepository;
    private SubjectRepository subjectRepository;
    private LessonRepository lessonRepository;
    private SystemLogRepository systemLogRepository;


    @Override
    public DashboardCouseraRateResponse countCouseraRate() {
        return null;
    }

    @Override
    public DashboardTotalUserEachWeekResponse countTotalUserEachWeek() {
        return null;
    }

    @Override
    public DashboardOverviewResponse countOverview() {
        Date dateStartOfThisMonth = Date.from(
                YearMonth.now().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        int totalUserNow = userRepository.countAllByStatus(UserStatus.ACTIVE);
        int totalUserBeforeThisMonth = userRepository.countAllByStatusAndCreatedAtBefore(UserStatus.ACTIVE, dateStartOfThisMonth);
        int totalSubjectNow = subjectRepository.countAllByStatus(SubjectStatus.ACTIVE);
        int totalSubjectBeforeThisMonth = subjectRepository.countAllByStatusAndCreatedAtBefore(SubjectStatus.ACTIVE, dateStartOfThisMonth);
        int totalLessonNow = lessonRepository.countAllByStatus(LessonStatus.PUBLIC);
        int totalLessonBeforeThisMonth  = lessonRepository.countAllByStatusAndCreatedAtBefore(LessonStatus.PUBLIC, dateStartOfThisMonth);
        int totalProgressSubjectComplete = progressSubjectRepository.countAllByProgressStatus(ProgressStatus.COMPLETED);
        int totalAllProgressSubjectComplete = progressSubjectRepository.countAll();
        double completionRateNow = totalAllProgressSubjectComplete * 100.0 / totalProgressSubjectComplete;
        int totalProgressSubjectCompleteBeforeThisMonth = progressSubjectRepository.countAllByProgressStatusAndEndDateBefore(ProgressStatus.COMPLETED, dateStartOfThisMonth);
        int totalAllProgressSubjectCompleteBeforeThisMonth = progressSubjectRepository.countAllByEndDateBefore(dateStartOfThisMonth);
        double completionRateBeforeThisMonth = totalProgressSubjectCompleteBeforeThisMonth * 100.0 / totalAllProgressSubjectCompleteBeforeThisMonth;
        int totalUserNormal = userRepository.countAllByStatusAndMembershipLevel(UserStatus.ACTIVE, MembershipLevel.NORMAL);
        int totalUserOneYear = userRepository.countAllByStatusAndMembershipLevel(UserStatus.ACTIVE, MembershipLevel.ONE_YEAR);
        int totalUserOneMonth = userRepository.countAllByStatusAndMembershipLevel(UserStatus.ACTIVE, MembershipLevel.ONE_MONTH);
        int totalUserSixMonths = userRepository.countAllByStatusAndMembershipLevel(UserStatus.ACTIVE, MembershipLevel.SIX_MONTHS);
        List<SystemLog> systemLogs = systemLogRepository.findTop7ByOrderByTimestampDesc();
        return DashboardOverviewResponse.builder()
                .totalUserNow(totalUserNow)
                .totalUserLastMonth(totalUserBeforeThisMonth)
                .totalSubjectNow(totalSubjectNow)
                .totalSubjectLastMonth(totalSubjectBeforeThisMonth)
                .totalLessonNow(totalLessonNow)
                .totalLessonLastMonth(totalLessonBeforeThisMonth)
                .completionRateSubjectNow(completionRateNow)
                .completionRateSubjectLastMonth(completionRateBeforeThisMonth)
                .totalUserNormal(totalUserNormal)
                .totalUserOneYear(totalUserOneYear)
                .totalUserOneMonth(totalUserOneMonth)
                .totalUserSixMonths(totalUserSixMonths)
                .systemLogs(systemLogs)
                .build();
    }
}
