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
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.ParentStudentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ParentStudentService;
import java.security.SecureRandom;
import java.util.Objects;
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
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        ParentStudent parentStudent = new ParentStudent().builder()
                .parent(Objects.requireNonNull(userRepository.findByEmail(email)).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)))
                .verificationCode(sb.toString())
                .build();
        parentStudentRepository.save(parentStudent);
        return sb.toString();

}

    @Override
    public String addParentStudent() {
        return "";
    }
}
