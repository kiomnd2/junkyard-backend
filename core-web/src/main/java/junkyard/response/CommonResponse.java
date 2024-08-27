package junkyard.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import junkyard.common.response.codes.Codes;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(String code, String message, T data) {

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(Codes.NORMAL.name(), Codes.NORMAL.getDescription(), data);
    }

    public static <T> CommonResponse<T> failed(String code, String message) {
        return new CommonResponse<>(code, message, null);
    }

}
