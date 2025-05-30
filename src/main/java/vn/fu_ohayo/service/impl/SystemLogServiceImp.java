package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SystemLogRequest;
import vn.fu_ohayo.dto.response.SystemLogResponse;
import vn.fu_ohayo.mapper.SystemLogMapper;
import vn.fu_ohayo.repository.SystemLogRepository;
import vn.fu_ohayo.service.SystemLogService;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SystemLogServiceImp implements SystemLogService {

    SystemLogRepository systemLogRepository;
    SystemLogMapper systemLogMapper;

    @Override
    public List<SystemLogResponse> getAllLogs() {
        return systemLogRepository.findAll()
                .stream()
                .map(systemLogMapper::toSystemLogResponse)
                .toList();
    }

    @Override
    public List<SystemLogResponse> searchSystemLog(SystemLogRequest request) {
        return systemLogRepository.findAll()
                .stream()
                .filter(log -> request.getStartTimestamp() == null
                        || !log.getTimestamp().isBefore(request.getStartTimestamp()))
                .filter(log -> request.getEndTimestamp() == null
                        || !log.getTimestamp().isAfter(request.getEndTimestamp()))
                .filter(log -> request.getAction() == null
                        || log.getAction().toLowerCase().contains(request.getAction().toLowerCase()))
                .filter(log -> request.getDetails() == null
                        || log.getDetails().toLowerCase().contains(request.getDetails().toLowerCase()))
                .filter(log -> request.getRole() == null
                        || log.getRole() == request.getRole())
                .map(systemLogMapper::toSystemLogResponse)
                .toList();
    }

}
