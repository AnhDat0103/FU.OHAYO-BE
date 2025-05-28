package vn.fu_ohayo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
