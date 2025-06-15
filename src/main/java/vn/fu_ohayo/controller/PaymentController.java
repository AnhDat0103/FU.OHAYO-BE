package vn.fu_ohayo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.PaymentRequest;
import vn.fu_ohayo.dto.response.AuthUrlResponse;
import vn.fu_ohayo.dto.response.MembershipResponse;
import vn.fu_ohayo.dto.response.PaymentResponse;
import vn.fu_ohayo.entity.MembershipLevel;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.MembershipMapper;
import vn.fu_ohayo.repository.MemberShipLevelOfUserRepository;
import vn.fu_ohayo.repository.MemberShipLevelRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;
import vn.fu_ohayo.service.PaymentService;
import vn.fu_ohayo.service.VnpayService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j( topic = "PaymentController")
public class PaymentController {
    VnpayService vnPayService;
    MemberShipLevelRepository memberShipLevelRepository;
    MembershipMapper membershipMapper;

    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        log.info(String.valueOf(paymentRequest.getAmount()));
        log.info(String.valueOf(paymentRequest.getUserId()));
        String url = vnPayService.createPaymentUrl(request, paymentRequest.getAmount(), paymentRequest.getNotificationId());
        PaymentResponse response = PaymentResponse.builder()
                .url(url)
                .build();
        log.info(response.getUrl());
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
    public ResponseEntity<?> getAllMembershipOfUser() {
        return ResponseEntity.ok().body(paymentService.paymentInfo());
    }

    @PostMapping("/sendToParent")
    public ResponseEntity<String> sendRequestToParent(@RequestBody PaymentRequest paymentRequest) {
        paymentService.sendRequestToParent(paymentRequest);
        return ResponseEntity.ok("oke");
    }

}
