package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.NotificationDTO;
import vn.fu_ohayo.entity.Notification;

@Mapper(componentModel = "spring")

public interface NotificationMapper {
    NotificationDTO notificationDTO(Notification notification);
}
