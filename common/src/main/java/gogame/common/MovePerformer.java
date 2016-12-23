package gogame.common;

import java.util.List;

public interface MovePerformer {
    void placeStone(Color color, int x, int y);
    void pass(Color color);
    void acceptScoring(Color color);
    void rejectScoring(Color color);
    void proposeAlive(List<Stone> alive);
    void proposeDead(List<Stone> dead);
    void addMoveGenerator(MoveGenerator generator) throws BoardFullException;
    void removeMoveGenerator(MoveGenerator generator);
    void surrender(Color color);
}
