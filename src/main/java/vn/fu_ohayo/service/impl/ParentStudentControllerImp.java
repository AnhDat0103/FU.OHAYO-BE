package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.service.ParentStudentController;
import java.security.SecureRandom;
@Controller

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParentStudentControllerImp implements ParentStudentController {
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int CODE_LENGTH = 6;
    @Override
    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();

}

    @Override
    public String addParentStudent() {
        return "";
    }
}
