package junkyard.response;

import io.jsonwebtoken.security.InvalidKeyException;
import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ResponseControllerAdvice {

    /**
     * http Status result 200 business Error
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BaseException.class)
    public CommonResponse<Void> onBaseException(BaseException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponse.failed(e.getCode().name(), e.getCode().getDescription(e.getArgs()));
    }

    /**
     * input valid exception
     * status 400 and result fail
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<String> onArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            return CommonResponse.failed(Codes.COMMON_REQUIRED_VALUE.name(),
                    Codes.COMMON_REQUIRED_VALUE.getDescription(fieldError.getField()));
        } else {
            return CommonResponse.failed(Codes.COMMON_REQUIRED_VALUE.name(), "UNKNOWN");
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidKeyException.class)
    public CommonResponse<String> onInvalidKeyException(InvalidKeyException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponse.failed(Codes.COMMON_REQUIRED_VALUE.name(), Codes.COMMON_INVALID_TOKEN_ERROR.getDescription());
    }


    /**
     * http status 500 and result fail
     * 시스템 예외
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> onException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponse.failed(Codes.COMMON_SYSTEM_ERROR.name(), Codes.COMMON_SYSTEM_ERROR.getDescription());
    }
}
