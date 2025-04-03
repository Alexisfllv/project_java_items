package corporation.proyect.exception.Errors;

public class ExInvalidDataException extends RuntimeException {
    //400 Bad Request
    public ExInvalidDataException(String message) {
        super(message);
    }
}