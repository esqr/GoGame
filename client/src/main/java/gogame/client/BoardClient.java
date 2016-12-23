package gogame.client;

import gogame.common.*;
import gogame.common.collections.ObservableBoard;

import java.util.List;

public class BoardClient implements MovePerformer {
    private ObservableBoard board;
    private MovePerformer performer;

    public BoardClient(MovePerformer performer, ObservableBoard board) {
        this.performer = performer;
        this.board = board;
    }

    @Override
    public void placeStone(Color color, int x, int y) {
        if (board.getStone(x, y) == Color.NONE) {
            performer.placeStone(color, x, y);
        }
    }

    @Override
    public void pass(Color color) {
        performer.pass(color);
    }

    @Override
    public void acceptScoring(Color color) {
        performer.acceptScoring(color);
    }

    @Override
    public void rejectScoring(Color color) {
        performer.rejectScoring(color);
    }

    @Override
    public void proposeAlive(List<Stone> alive) {
        if (alive.size() > 0 && alive.get(0).getColor() != Color.NONE) {
            performer.proposeAlive(alive);
        }
    }

    @Override
    public void proposeDead(List<Stone> dead) {
        if (dead.size() > 0 && dead.get(0).getColor() != Color.NONE) {
            performer.proposeDead(dead);
        }
    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void surrender(Color color) {
        performer.surrender(color);
    }

    public void setBoardSize(int boardSize) {
        board.setSize(boardSize);
    }
}
