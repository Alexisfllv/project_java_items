package corporation.proyect.exception.Errors;

public class ExAccesoNoAutorizadoException extends RuntimeException {
    // 401 Unauthorized 403 Forbidden
    public ExAccesoNoAutorizadoException(String message) {
        super(message);
    }
}