package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ParentStudentService;
import java.security.SecureRandom;
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParentStudentServiceImp implements ParentStudentService {
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int CODE_LENGTH = 6;
    UserRepository userRepository;
    @Override
    public String generateCode() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
//        ParentStudent parentStudent = new ParentStudent().builder().parent().build();

        return sb.toString();

}

    @Override
    public String addParentStudent() {
        return "";
    }
}
