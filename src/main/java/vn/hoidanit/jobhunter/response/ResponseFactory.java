package vn.hoidanit.jobhunter.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    // SUCCESS with custom HttpStatus
    public static <T> ResponseEntity<RestResponse<T>> success(
            T data, String message, HttpStatus httpStatus) {

        RestResponse<T> response = new RestResponse<>();
        response.setStatusCode(httpStatus.value());
        response.setData(data);
        response.setMessage(message);
        // error is null by default
        return new ResponseEntity<>(response, httpStatus);
    }

    // SUCCESS with default 200 OK
    public static <T> ResponseEntity<RestResponse<T>> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    // For DELETE or NO_CONTENT cases
    public static <T> ResponseEntity<RestResponse<T>> noContent(String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    // ERROR patterns if you want them here too
    public static <T> ResponseEntity<RestResponse<T>> error(
            String errorMsg, HttpStatus status, String message) {

        RestResponse<T> response = new RestResponse<>();
        response.setStatusCode(status.value());
        response.setError(errorMsg);
        response.setMessage(message);
        return new ResponseEntity<>(response, status);
    }
}
