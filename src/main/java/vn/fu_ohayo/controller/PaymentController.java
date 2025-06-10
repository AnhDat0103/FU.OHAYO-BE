package vn.fu_ohayo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.PaymentRequest;
import vn.fu_ohayo.dto.response.AuthUrlResponse;
import vn.fu_ohayo.dto.response.MembershipResponse;
import vn.fu_ohayo.dto.response.PaymentResponse;
import vn.fu_ohayo.entity.MembershipLevel;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.mapper.MembershipMapper;
import vn.fu_ohayo.repository.MemberShipLevelOfUserRepository;
import vn.fu_ohayo.repository.MemberShipLevelRepository;
import vn.fu_ohayo.service.PaymentService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j( topic = "PaymentController")
public class PaymentController {
    PaymentService vnPayService;
    MemberShipLevelRepository memberShipLevelRepository;
    MembershipMapper membershipMapper;
    MemberShipLevelOfUserRepository mouRepository;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        log.info(String.valueOf(paymentRequest.getAmount()));
        log.info(String.valueOf(paymentRequest.getUserId()));
        String url = vnPayService.createPaymentUrl(request, paymentRequest.getAmount(), paymentRequest.getUserId());
        PaymentResponse response = PaymentResponse.builder()
                .url(url)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllMembership")
    public ResponseEntity<List<MembershipResponse>> getAllMembership() {
        List<MembershipLevel> entities = memberShipLevelRepository.findByIdNotIn(Collections.singletonList(4L));
        List<MembershipResponse> responses = entities.stream()
                .map(membershipMapper::toMembershipResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(HttpServletRequest request) {
        String result = vnPayService.processVNPayReturn(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMembershipOfUser")
    public ResponseEntity<MembershipLevelOfUser> getAllMembershipOfUser(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(mouRepository.findByUserUserId(Long.parseLong(userId)));
    }

}
