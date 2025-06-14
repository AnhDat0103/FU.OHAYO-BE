package vn.fu_ohayo.dto.response;


import lombok.Data;
import vn.fu_ohayo.enums.NotificationEnum;

import java.util.Date;
@Data
public class NotificationDTO {
    private int notificationId;

    private String title;

    private String content;

    private Date createdAt;

    private Long userId;

    private boolean statusSend;

    private boolean status;

    private NotificationEnum type;
}
