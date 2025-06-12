package vn.fu_ohayo.enums;

import lombok.Getter;

@Getter
public enum NotificationEnum {

    NORMAL("You have new notification"),
    PAYMENT("A payment is requested for your child"),
    ACCEPT_STUDENT("Your child's registration needs confirmation");

    private final String title;

    NotificationEnum(String title) {
        this.title = title;
    }

}
