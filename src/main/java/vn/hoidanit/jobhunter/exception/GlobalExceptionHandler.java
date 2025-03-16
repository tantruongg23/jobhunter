package vn.hoidanit.jobhunter.exception;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<String>> handleIdException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(), HttpStatus.BAD_REQUEST, "Exception occurs...");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseFactory.error("User not found", HttpStatus.NOT_FOUND, ex.getMessage());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleArgumentValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errors = new LinkedList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }

        Object message = (errors.size() > 1 ? errors : errors.get(0));
        return ResponseFactory.error(ex.getBody().getDetail(), HttpStatus.BAD_REQUEST, message);
    }
}
