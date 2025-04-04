package vn.hoidanit.jobhunter.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

public class RestResponseBuilder {
    public static <T> ResponseEntity<RestResponse<T>> success(T data, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage(message);
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<RestResponse<T>> error(HttpStatus status, String error, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setStatusCode(status.value());
        // ApiMessage message2 = returnType.getMethodAnnotation(ApiMessage.class);
        response.setMessage(message);
        response.setError(error);

        return ResponseEntity.status(status).body(response);
    }
}
