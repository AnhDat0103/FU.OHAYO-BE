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
    INVALID_CONTENT_SPEAKING("403", "ContentSpeaking not found" ),

    SUBJECT_NOT_FOUND("4004", "Subject not found"),
    SUBJECT_CODE_EXISTS("4004", "Subject code is existed" ),
    EXERCISE_NOT_FOUND("4005", "Exercise not found"),

    SUBJECT_NAME_EXISTS("4006",  "Subject name is existed"),
    SUBJECT_IN_USE("4007", "Subject is in use, cannot be deleted"),
    LESSON_NOT_FOUND("4008",  "Lesson not found"),
    LESSON_NAME_EXIST( "4009", "Lesson is exited with this name." ),
    LESSON_HAS_VOCABULARY("4010", "Lesson has vocabularies, cannot be deleted"),
    LESSON_HAS_GRAMMAR("4011","Lesson has grammars, cannot be deleted" );

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
    public static final String NOT_EMPTY_CONTENT_TYPE = "Content type cannot be null";
    public static final String NOT_EMPTY_CATEGORY = "Category cannot be null";
    public static final String NOT_EMPTY_IMAGE = "Image cannot be null";
    public static final String NOT_EMPTY_URL = "File URL can not null";
    public static final String NOT_EMPTY_SUBJECT_CODE = "Subject code must not be empty";
    public static final String NOT_EMPTY_SUBJECT_NAME = "Subject name must not be empty";
    public static final String INVALID_SUBJECT_NAME = "Subject name must be less than 50 characters";
    public static final String NOT_EMPTY_JLPT_LEVEL = "JLPT level must not be empty";
    public static final String NOT_EMPTY_PART_OF_SPEECH = "Part of speech must not be empty";
    public static final String Not_EMPTY_MEANING = "Meaning must not be null";
    public static final String NOT_EMPTY_ROMAJI = "Romaji must not be empty";

    public static final String NOT_EMPTY_KANA = "Kana must not be empty";
    public static final String MAX_LENGTH_50 = "input be less than 50 characters";
    public static final String NOT_EMPTY_KANJI = "Kanji must not be empty";
    public static final String NOT_EMPTY_TITLE = "Title must not be empty";
    public static final String MAX_LENGTH_100 = "input must be less than 100 characters";
    public static final String NOT_EMPTY_STRUCTURE = "Structure must not be empty";
    public static final String MAX_LENGTH_200 = "input must be less than 200 characters";
    public static final String MAX_LENGTH_500 = "input must be less than 500 characters";

    private final String code;
    private final String message;
}
