package cl.tomas.naivepay.domain.exceptions;

public class ApiForbiddenException extends ApiRequestException{

    public ApiForbiddenException(String message) {
        super(message);
    }
    
}
