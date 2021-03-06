package gogame.client;

import gogame.common.*;

import java.util.List;

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

    @Override
    public void opponentSurrendered() {
        forwardee.opponentSurrendered();
    }

    @Override
    public void scoringStarted() {
        forwardee.scoringStarted();
    }

    @Override
    public void aliveProposed(List<Stone> alive) {
        forwardee.aliveProposed(alive);
    }

    @Override
    public void deadProposed(List<Stone> dead) {
        forwardee.deadProposed(dead);
    }

    public void setForwardee(MoveGenerator forwardee) {
        this.forwardee = forwardee;
    }
}
