package vn.fu_ohayo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/options")
public class OptionController {
    @GetMapping("/user-statuses")
    public List<UserStatus> getUserStatuses() {
        return Arrays.asList(UserStatus.values());
    }

    @GetMapping("/membership-levels")
    public List<MembershipLevel> getMembershipLevels() {
        return Arrays.asList(MembershipLevel.values());
    }
}
