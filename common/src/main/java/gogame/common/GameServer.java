package gogame.common;

import java.util.ArrayList;
import java.util.List;

public class GameServer {
    public enum Filter {
        ALL,
        EMPTY,
        NOT_FULL
    }

    public static final Integer NEW_BOARD = -1;

    public void addPlayerToBoard(MoveGenerator player, Integer boardIndex) {}
    public List<Integer> getBoardsList(Filter filter) {
        return new ArrayList<>();
    }
}
