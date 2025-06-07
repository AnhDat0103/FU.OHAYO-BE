package vn.fu_ohayo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {

    // 1xxx - validation
    INVALID_FIELDS("1001","Invalid fields"),
    INVALID_EMAIL("1002","Email is invalid"),
    INVALID_CATEGORY_CONTENT_READING("1003","Choose right category contentt" ),
    INVALID_ANSWER_CORRECT_COUNT("1004","Have to choose exactly one correct answer for the question." ),

    // 2xxx - authentication
    EMAIL_EXIST("2001","Email is used to register" ),
    PHONE_EXIST("2002", "Phone is used to register."),
    USERNAME_EXIST("2003","Username is used to register" ),
    EMAIL_OR_PASSWORD_INCORRECT("2004", "Email or password is incorrect."),

    // 3xxx - authorization


    // 4xxx - not found
    USER_NOT_FOUND("4001","User not found"),
    ROLE_NOT_FOUND("4002","Role not found"),
    INVALID_TOKEN("4003", "Invalid token"),
    REFRESH_TOKEN_NOT_FOUND ("4003", "Refresh token not found"),
    INTERNAL_SERVER_ERROR("5000", "Internal server error"),
    INVALID_CONTENT_SPEAKING("403", "ContentSpeaking not found" ),

    SUBJECT_NOT_FOUND("4004", "Subject not found"),
    SUBJECT_CODE_EXISTS("4004", "Subject code is existed" ),
    EXERCISE_NOT_FOUND("4005", "Exercise not found"),
    QUESTION_NOT_FOUND("4006","Question not found" ),
    SUBJECT_NAME_EXISTS("4006",  "Subject name is existed"),
    SUBJECT_IN_USE("4007", "Subject is in use, cannot be deleted"),
    INVALID_PAGE("4008","Page is not exist" ),

    LESSON_NOT_FOUND("4008",  "Lesson not found"),
    LESSON_NAME_EXIST( "4009", "Lesson is exited with this name." ),
    LESSON_HAS_VOCABULARY("4010", "Lesson has vocabularies, cannot be deleted"),
    LESSON_HAS_GRAMMAR("4011","Lesson has grammars, cannot be deleted" ),

    VOCABULARY_EXISTS("4012", "Vocabulary already exists in the lesson"),
    VOCABULARY_NOT_FOUND("4013", "Vocabulary not found with this kanji." ),
    GRAMMAR_EXISTED("4014", "Grammar is  existed in this lesson." ),
    GRAMMAR_NOT_FOUND("4015", "Grammar not found with this title." ),
    CONTENT_READING_NOT_FOUND("4016", "Content reading not found"),
    VOCABULARY_ALREADY_EXISTS_IN_CONTENT_READING("2017","Vocabulary is  existed in content reading " ),
    GRAMMAR_ALREADY_EXISTS_IN_CONTENT_READING("2017","Grammar is  existed in content reading " );


    // 5xxx - server error

    //message validation
    public static final String INVALID_STATUS_MS = "Status is invalid";
    public static final String INVALID_MEMBERSHIP_MS = "Membership level is invalid";
    public static final String INVALID_EMAIL_MS = "Email is invalid";
    public static final String NOT_EMPTY_EMAIL = "Email must not be empty";
    public static final String NOT_EMPTY_PASSWORD = "Password must not be empty";
    public static final String INVALID_PASSWORD = "Password must be at least 5 characters";
    public static final String INVALID_NAME = "Full name must be less than 50 characters";
    public static final String NOT_EMPTY_NAME = "Full name cannot be null";
    public static final String INVALID_PHONE = "Phone number must be between 10 and 12 digits";
    public static final String INVALID_ADDRESS = "Address must be less than 255 characters";
    public static final String INVALID_URL_AVATAR = "Avatar URL must be less than 255 characters";
    public static final String INVALID_URL_AUDIO = "File audio must be less than 255 characters";

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
    public static final String NOT_EMPTY_MEANING = "Meaning must not be null";
    public static final String NOT_EMPTY_ROMAJI = "Romaji must not be empty";
    public static final String NOT_EMPTY_SCRIPT = "script can not null";
    public static final String NOT_EMPTY_DATE = "Date can not null";

    public static final String NOT_EMPTY_KANA = "Kana must not be empty";
    public static final String MAX_LENGTH_50 = "input be less than 50 characters";
    public static final String NOT_EMPTY_KANJI = "Kanji must not be empty";
    public static final String NOT_EMPTY_TITLE = "Title must not be empty";
    public static final String MAX_LENGTH_100 = "input must be less than 100 characters";
    public static final String NOT_EMPTY_STRUCTURE = "Structure must not be empty";
    public static final String MAX_LENGTH_200 = "input must be less than 200 characters";
    public static final String MAX_LENGTH_500 = "input must be less than 500 characters";
    public static final String MIN_TIME_1 = "Time must be greater than 1 minute";

    private final String code;
    private final String message;
}
