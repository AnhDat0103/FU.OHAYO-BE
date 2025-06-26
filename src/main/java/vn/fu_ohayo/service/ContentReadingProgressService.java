package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ApiResponse;

public interface ContentReadingProgressService {
    ApiResponse<String> markReadingProgress(Long userId, Long contentReadingId);
    Boolean isDoneReading(Long userId, Long contentReadingId);
}
