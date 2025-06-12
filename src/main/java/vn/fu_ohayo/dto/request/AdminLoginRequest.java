package vn.fu_ohayo.dto.request;

import lombok.Data;

@Data
public class AdminLoginRequest {
    String email;
    String password;
}
