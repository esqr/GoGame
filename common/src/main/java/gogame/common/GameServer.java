package gogame.common;

import java.util.ArrayList;
import java.util.List;

public class GameServer {
    public enum Filter {
        ALL,
        EMPTY,
        NOT_FULL
    }

    private static volatile GameServer instance = null;
    public static final Integer NEW_BOARD = -1;

    private GameServer() {}
    public void addPlayerToBoard(MoveGenerator player, Integer boardIndex) {}
    public List<Integer> getBoardsList(Filter filter) {
        return new ArrayList<Integer>();
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
