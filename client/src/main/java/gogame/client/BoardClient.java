package gogame.client;

import gogame.common.Color;
import gogame.common.MoveGenerator;
import gogame.common.MovePerformer;
import gogame.common.Scoring;
import gogame.common.collections.ObservableBoard;

public class BoardClient implements MovePerformer {
    private ObservableBoard board;
    private MovePerformer performer;

    public BoardClient(MovePerformer performer, ObservableBoard board) {
        this.performer = performer;
    }

    @Override
    public void placeStone(Color color, int x, int y) {
        if (board.getStone(x, y) == null) {
            performer.placeStone(color, x, y);
        }
    }

    @Override
    public void pass(Color color) {
        performer.pass(color);
    }

    @Override
    public void proposeScoring(Scoring scoring) {
        performer.proposeScoring(scoring);
    }

    @Override
    public void acceptScoring(Scoring scoring) {
        performer.acceptScoring(scoring);
    }

    @Override
    public void rejectScoring() {
        performer.rejectScoring();
    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }
}
