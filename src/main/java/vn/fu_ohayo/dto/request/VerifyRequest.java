package vn.fu_ohayo.dto.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String clientId;
    private String parentCode;
}
