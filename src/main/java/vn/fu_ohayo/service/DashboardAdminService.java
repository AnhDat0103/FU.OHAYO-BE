package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.DashboardCouseraRateResponse;
import vn.fu_ohayo.dto.response.DashboardOverviewResponse;
import vn.fu_ohayo.dto.response.DashboardTotalUserEachWeekResponse;

public interface DashboardAdminService {
    DashboardCouseraRateResponse countCouseraRate ();
    DashboardTotalUserEachWeekResponse countTotalUserEachWeek();
    DashboardOverviewResponse countOverview();

}
