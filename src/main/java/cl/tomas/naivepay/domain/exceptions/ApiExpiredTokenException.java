package cl.tomas.naivepay.domain.exceptions;

public class ApiExpiredTokenException extends RuntimeException{
    public ApiExpiredTokenException(){
        super("Token Expired!");
    }
}
