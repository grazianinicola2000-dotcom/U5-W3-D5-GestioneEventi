package nicolagraziani.U5_W3_D5_GestioneEventi.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
