package vn.fu_ohayo.exception;


import lombok.Getter;
import lombok.Setter;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.List;

@Getter
@Setter
public class AppException extends RuntimeException {

    private String code;
    private List<FieldValidateException> fieldValidateExceptions;

    public AppException(ErrorEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public AppException(String message, String code, List<FieldValidateException> fieldValidateExceptions) {
        super(message);
        this.code = code;
        this.fieldValidateExceptions = fieldValidateExceptions;
    }

}
