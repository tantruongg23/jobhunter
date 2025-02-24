package vn.hoidanit.jobhunter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(value = IdInvalidException.class)
    // public ResponseEntity<RestResponse<String>>
    // handleIdException(IdInvalidException idInvalidException) {
    // RestResponse<String> response = new RestResponse<>();
    // response.setStatusCode(HttpStatus.BAD_REQUEST.value());
    // response.setMessage("IdInvalidException");
    // response.setError(idInvalidException.getMessage());

    // return ResponseEntity.badRequest().body(response);
    // }

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<String>> handleIdException(IdInvalidException ex) {
        return ResponseFactory.error("ID is invalid", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseFactory.error("ID is invalid", HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // @ExceptionHandler(EntityNotFoundException.class)
    // public ResponseEntity<RestResponse<?>>
    // handleEntityNotFound(EntityNotFoundException entityNotFoundException) {
    // RestResponse<Object> response = new RestResponse<>();
    // response.setStatusCode(HttpStatus.NOT_FOUND.value());
    // response.setMessage(entityNotFoundException.getMessage());
    // response.setError("Entity not found");

    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    // }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleCommonException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
