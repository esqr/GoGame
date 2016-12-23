package gogame.common;

import java.util.List;

public interface MoveGenerator {
    void colorSet(Color color);
    void yourTurnBegan();
    void yourMoveValidated(boolean valid);
    void stonePlaced(Color color, int x, int y);
    void passed(Color color);
    void scoringAccepted(Scoring scoring);
    void scoringRejected();
    void setMovePerformer(MovePerformer performer);
    void opponentDisconnected();
    void opponentSurrendered();
    void scoringStarted();
    void aliveProposed(List<Stone> alive);
    void deadProposed(List<Stone> dead);
}
