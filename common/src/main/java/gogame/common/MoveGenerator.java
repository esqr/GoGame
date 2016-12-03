package gogame.common;

public interface MoveGenerator {
    void colorSet(Color color);
    void yourTurnBegan();
    void yourMoveValidated(boolean valid);
    void stonePlaced(Color color, int x, int y);
    void passed(Color color);
    void scoringProposed(Scoring scoring);
    void scoringAccepted(Scoring scoring);
    void scoringRejected();
    void setMovePerformer(MovePerformer performer);
}
