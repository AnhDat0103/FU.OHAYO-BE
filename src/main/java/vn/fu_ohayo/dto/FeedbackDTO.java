package vn.fu_ohayo.dto;

import lombok.Data;
import vn.fu_ohayo.dto.response.UserResponse;

import java.util.Date;

@Data
public class FeedbackDTO {
    private int feedbackId;
    private UserResponse user;
    private String content;
    private int rating;
    private Date createdAt;
}
