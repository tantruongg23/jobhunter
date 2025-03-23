package vn.hoidanit.jobhunter.exception;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            IdInvalidException.class,
            EmailExistedException.class,
            IllegalStateException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })

    public ResponseEntity<RestResponse<String>> handleCommonCRUDException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(), HttpStatus.BAD_REQUEST, "Error occurs...");
    }

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            MissingServletRequestPartException.class
    })
    public ResponseEntity<RestResponse<String>> handleIdException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(), HttpStatus.BAD_REQUEST, "Exception occurs...");
    }

    // Xử lý lỗi BadJwtException và trả về 401 Unauthorized
    @ExceptionHandler(BadJwtException.class)
    public ResponseEntity<RestResponse<Object>> handleBadJwtException(BadJwtException ex) {
        RestResponse<Object> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        response.setError(ex.getMessage());
        response.setMessage("Token is invalid or malformed");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<RestResponse<Object>> handleNotFound(Exception ex) {
        return ResponseFactory.error(
                "Resource not found",
                HttpStatus.NOT_FOUND,
                "404 not found. The URL you requested was not found.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleCommonException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = {
            StorageException.class,
            MaxUploadSizeExceededException.class
    })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        return ResponseFactory.error(ex.getMessage(),
                HttpStatus.BAD_REQUEST, "Exception occurs when uploading file...");
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
