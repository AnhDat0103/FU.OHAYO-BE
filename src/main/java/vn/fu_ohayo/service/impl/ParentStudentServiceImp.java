package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ParentCodeStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.ParentStudentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ParentStudentService;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "ParentStudent")
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParentStudentServiceImp implements ParentStudentService {
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int CODE_LENGTH = 6;
    UserRepository userRepository;
    ParentStudentRepository parentStudentRepository;
    @Override
    public String generateCode() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Email lafa" ,email);
        SecureRandom random = new SecureRandom();
        StringBuilder sb ;
        boolean isDuplicate = false;
        List<ParentStudent> parentStudents = parentStudentRepository.findAll();
        do {
            sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }

            isDuplicate = parentStudentRepository.existsByVerificationCode(sb.toString());

        } while (isDuplicate);

        ParentStudent parentStudent = ParentStudent.builder()
                .parent(userRepository.findByEmail(email)
                        .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)))
                .verificationCode(sb.toString())
                .build();
        parentStudentRepository.save(parentStudent);
        return sb.toString();

}
    @Override
    public String addParentStudent() {
        return "";
    }

    @Override
    public String extractCode(String code, Long id) {
        var parentStudent1 = parentStudentRepository.findByVerificationCode(code);

        if(parentStudent1.getParent() == null) {
            return "The code isn't existed.";
        }

        Optional<ParentStudent> parentStudent = parentStudentRepository.findByVerificationCodeAndStudentUserId(code, id);

            if(parentStudent.isPresent() && parentStudent.get().getParentCodeStatus().equals(ParentCodeStatus.PENDING) ){
                return "Please check the notifications on your parent's page";
            }
            else if(parentStudent.get().getParentCodeStatus().equals(ParentCodeStatus.REJECT)) {
                return "Code declined by your parent. Ask them to generate a new one";
        }
            else if(parentStudent.get().getParentCodeStatus().equals(ParentCodeStatus.CONFIRM)) {
                return "Code verified. Please wait...";
            }
            if(parentStudent1.getVerificationCode() != null ) {
            return "The code already exists. Please try a different one.";
             }
            Optional<User> user = userRepository.findById(id);
            parentStudent1.setStudent(user.get());
            parentStudent1.setParentCodeStatus(ParentCodeStatus.PENDING);

        return "";
    }
}
