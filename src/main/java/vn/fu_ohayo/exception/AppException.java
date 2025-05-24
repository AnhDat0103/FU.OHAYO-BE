package vn.fu_ohayo.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppException extends RuntimeException {

    private String code;
    private List<FieldValidateException> fieldValidateExceptions;


    public AppException(String message, String code, List<FieldValidateException> fieldValidateExceptions) {
        super(message);
        this.code = code;
        this.fieldValidateExceptions = fieldValidateExceptions;
    }

}
