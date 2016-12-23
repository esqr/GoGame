package gogame.client;

public class InvalidBoardSizeException extends Exception {
    public InvalidBoardSizeException() {
        super();
    }

    public InvalidBoardSizeException(String msg) {
        super(msg);
    }
}
