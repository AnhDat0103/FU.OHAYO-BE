package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.SystemLogRequest;
import vn.fu_ohayo.dto.response.SystemLogResponse;

import java.util.List;

public interface SystemLogService {
    List<SystemLogResponse> getAllLogs();
    List<SystemLogResponse> searchSystemLog(SystemLogRequest request);
}
