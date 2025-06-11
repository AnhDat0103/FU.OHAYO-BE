package vn.fu_ohayo.dto.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private Long clientId;
    private String parentCode;
}
