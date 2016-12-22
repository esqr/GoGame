package gogame.common;

public interface MovePerformer {
    void placeStone(Color color, int x, int y);
    void pass(Color color);
    void proposeScoring(Scoring scoring);
    void acceptScoring(Scoring scoring);
    void rejectScoring(Color color);
    void addMoveGenerator(MoveGenerator generator) throws BoardFullException;
    void removeMoveGenerator(MoveGenerator generator);
}
