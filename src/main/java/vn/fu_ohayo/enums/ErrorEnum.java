package vn.fu_ohayo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {

    // 1xxx - validation
    INVALID_FIELDS("1001","Invalid fields"),
    INVALID_EMAIL("1002","Email is invalid"),

    // 2xxx - authentication
    EMAIL_EXIST("2001","Email is used to register" ),
    PHONE_EXIST("2002", "Phone is used to register."),
    EMAIL_OR_PASSWORD_INCORRECT("2004", "Email or password is incorrect."),
    // 3xxx - authorization


    // 4xxx - not found
    USER_NOT_FOUND("4001","User not found"),
    ROLE_NOT_FOUND("4002","Role not found"),
    POST_NOT_FOUND("4003"," Post not found" );

    ;

    // 5xxx - server error

    //message validation
    public static final String INVALID_EMAIL_MS = "Email is invalid";
    public static final String NOT_EMPTY_EMAIL = "Email must not be empty";
    public static final String NOT_EMPTY_PASSWORD = "Password must not be empty";
    public static final String INVALID_PASSWORD = "Password must be at least 5 characters";
    public static final String INVALID_NAME = "Full name must be less than 50 characters";
    public static final String NOT_EMPTY_NAME = "Full name cannot be null";
    public static final String INVALID_PHONE = "Phone number must be between 10 and 12 digits";
    public static final String INVALID_ADDRESS = "Address must be less than 255 characters";
    public static final String INVALID_URL_AVATAR = "Avatar URL must be less than 255 characters";
    public static final String NOT_EMPTY_USER = "User cannot be null";
    public static final String MAX_LENGTH_IMAGE = "Image URL must be less than 255 characters";

    private final String code;
    private final String message;
}
