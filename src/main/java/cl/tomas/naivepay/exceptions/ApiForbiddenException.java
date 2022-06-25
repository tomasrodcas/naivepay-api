package cl.tomas.naivepay.exceptions;

public class ApiForbiddenException extends ApiRequestException{

    public ApiForbiddenException(String message) {
        super(message);
    }
    
}
