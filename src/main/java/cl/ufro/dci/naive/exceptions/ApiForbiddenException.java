package cl.ufro.dci.naive.exceptions;

public class ApiForbiddenException extends ApiRequestException{

    public ApiForbiddenException(String message) {
        super(message);
    }
    
}
