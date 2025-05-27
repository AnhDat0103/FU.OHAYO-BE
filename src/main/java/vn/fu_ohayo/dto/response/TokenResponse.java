package vn.fu_ohayo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TokenResponse {
    String accessToken;
    String refreshToken;
}
