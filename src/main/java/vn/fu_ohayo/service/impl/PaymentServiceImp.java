package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.dto.request.PaymentRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.PaymentInfoResponse;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.MemberShipLevelOfUserRepository;
import vn.fu_ohayo.repository.ParentStudentRepository;
import vn.fu_ohayo.repository.PaymentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;
import vn.fu_ohayo.service.PaymentService;
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImp implements PaymentService {
    MemberShipLevelOfUserRepository memberShipLevelOfUserRepository;
    UserRepository userRepository ;
    ParentStudentRepository parentStudentRepository;
    PaymentRepository paymentRepository;
    @Override
    public ApiResponse<String> paymentInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        MembershipLevelOfUser membershipLevelOfUser = memberShipLevelOfUserRepository.findByUserUserId(user.getUserId());
        String mess = "";
        if(membershipLevelOfUser != null) {
            mess += "Your monthly subscription is valid until " + membershipLevelOfUser.getEndDate();
        }
        return ApiResponse.<String>builder().status("OK").message("Sucess").data(mess).code("200").build();
    }

    @Override
    public boolean createOrder() {
        return false;
    }

    @Override
    public void sendRequestToParent(PaymentRequest paymentRequest) {

    }

}
