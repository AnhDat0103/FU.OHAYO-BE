package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ApiResponse;

public interface PaymentService {
    ApiResponse<String> paymentInfo();
}
