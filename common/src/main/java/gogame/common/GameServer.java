package gogame.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {
    public enum Filter {
        ALL,
        EMPTY,
        NOT_FULL
    }

    private static volatile GameServer instance = null;
    public static final Integer NEW_BOARD = -1;
    private Map<String, Room> rooms;

    private GameServer() {
        rooms = new ConcurrentHashMap<>();
    }
    public void addPlayerToBoard(MoveGenerator player, String boardName) throws BoardFullException {
        Room room = rooms.get(boardName);
        room.getBoard().addMoveGenerator(player);
        player.setMovePerformer(room.getBoard());
    }

    public List<Room> getRoomList(Filter filter) {
        List<Room> filteredList = new ArrayList<>();
        for (Room room : rooms.values()) {
            int playerNumber = room.getBoard().getPlayerNumber();
            if ((filter == Filter.EMPTY && playerNumber == 0) ||
                    (filter == Filter.NOT_FULL && playerNumber < 2) ||
                    filter == Filter.ALL) {
                filteredList.add(room);
            }
        }

        return filteredList;
    }

    public void newBoard(String boardName, int boardSize) throws BoardExistsException {
        Room room = new Room(new Board(boardSize), boardName);
        if (rooms.putIfAbsent(boardName, room) != null) {
            throw new BoardExistsException();
        }
    }

    public void removeRoom(Room room) {
        rooms.remove(room.getName());
    }

    public static GameServer getInstance() {
        if (instance == null) {
            synchronized (GameServer.class) {
                if (instance == null) {
                    instance = new GameServer();
                }
            }
        }
        return instance;
    }
}
