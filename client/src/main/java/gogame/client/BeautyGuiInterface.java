package gogame.client;

import gogame.common.*;

public class BeautyGuiInterface implements MoveGenerator {
    private MoveGenerator forwardee; // employer -> employee, forwarder -> forwardee?

    @Override
    public void colorSet(Color color) {
        forwardee.colorSet(color);
    }

    @Override
    public void yourTurnBegan() {
        forwardee.yourTurnBegan();
    }

    @Override
    public void yourMoveValidated(boolean valid) {
        forwardee.yourMoveValidated(valid);
    }

    @Override
    public void stonePlaced(Color color, int x, int y) {
        forwardee.stonePlaced(color, x, y);
    }

    @Override
    public void passed(Color color) {
        forwardee.passed(color);
    }

    @Override
    public void scoringProposed(Scoring scoring) {
        forwardee.scoringProposed(scoring);
    }

    @Override
    public void scoringAccepted(Scoring scoring) {
        forwardee.scoringAccepted(scoring);
    }

    @Override
    public void scoringRejected() {
        forwardee.scoringRejected();
    }

    @Override
    public void setMovePerformer(MovePerformer performer) {

    }

    @Override
    public void opponentDisconnected() {
        forwardee.opponentDisconnected();
    }

    public void setForwardee(MoveGenerator forwardee) {
        this.forwardee = forwardee;
    }
}
