package gogame.common;

public final class CommunicationConstants {
    // prevent instantiation
    private CommunicationConstants() {}

    public static final String HELLO = "HELLO";
    public static final String BYE = "BYE";
    public static final String GET_BOARDS = "GET_BOARDS";
    public static final String JOIN_BOARD = "JOIN_BOARD";
    public static final String NEW_BOARD = "NEW_BOARD";
    public static final String PASS = "PASS";
    public static final String BOARD_LIST = "BOARDS";
    public static final String ERROR = "ERROR";
    public static final String JOINED = "JOINED";
    public static final String COLOR_SET = "COLOR";
    public static final String YOUR_TURN = "YOUR_TURN";
    public static final String MOVE_VALIDATED = "VALIDATED";
    public static final String OK = "OK";
    public static final String WRONG = "WRONG";
    public static final String STONE_PLACED = "STONE";
    public static final String PASSED = PASS;
    public static final String OPPONENT_DISCONNECTED = "OPPONENT_DISCONNECTED";

    public final class Errors {
        // prevent instantiation
        private Errors() {}

        public static final String BOARD_EXISTS = "BOARD_EXISTS";
        public static final String BOARD_FULL = "BOARD_FULL";
    }
}
