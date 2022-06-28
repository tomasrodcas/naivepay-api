package cl.tomas.naivepay.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    //Handles Specific ApiRequestException for Customized Exceptions
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    //Handles All Requests
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(HttpServletRequest req, Exception e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {ApiForbiddenException.class})
    public ResponseEntity<Object> handleForbiddenException(HttpServletRequest req, Exception e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.FORBIDDEN, 
                ZonedDateTime.now(ZoneId.of("Z")));
    
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }

}
