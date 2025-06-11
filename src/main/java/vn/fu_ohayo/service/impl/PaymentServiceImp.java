package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.dto.response.PaymentInfoResponse;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.repository.MemberShipLevelOfUserRepository;
import vn.fu_ohayo.repository.PaymentRepository;
import vn.fu_ohayo.service.PaymentService;
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImp implements PaymentService {
    PaymentRepository paymentRepository;
    MemberShipLevelOfUserRepository memberShipLevelOfUserRepository;
    @Override
    public PaymentInfoResponse paymentInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        MembershipLevelOfUser membershipLevelOfUser = memberShipLevelOfUserRepository.fin
//        PaymentInfoResponse paymentResponse = PaymentInfoResponse.builder()
//                .endDate().membershipLevel().build();
        return null;
    }
}
