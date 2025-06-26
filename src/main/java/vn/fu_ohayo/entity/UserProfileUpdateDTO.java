package vn.fu_ohayo.entity;

import lombok.Getter;
import lombok.Setter;
import vn.fu_ohayo.enums.Gender;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class UserProfileUpdateDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
}
