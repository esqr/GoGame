package gogame.common;

public class Room {
    private Board board;
    private String name;

    public Room(Board board, String name) {
        this.board = board;
        board.setRoom(this);
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }
}
