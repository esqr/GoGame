package gogame.common;

public interface MovePerformer {
    void placeStone(Color color, int x, int y);
    void pass(Color color);
    void proposeScoring(Scoring scoring);
    void acceptScoring(Scoring scoring);
    void rejectScoring();
    void addMoveGenerator(MoveGenerator generator);
    void removeMoveGenerator(MoveGenerator generator);
}
