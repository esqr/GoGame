package gogame.common;

public class Board implements MovePerformer {
    private Color[][] stones;
    private MoveGenerator black = null;
    private MoveGenerator white = null;

    public Board(int size) {
        stones = new Color[size][size];
    }

    private MoveGenerator player(Color color) {
        if (color == Color.BLACK) {
            return black;
        } else {
            return white;
        }
    }

    private MoveGenerator opponent(Color color) {
        if (color == Color.BLACK) {
            return white;
        } else {
            return black;
        }
    }

    @Override
    public void placeStone(Color color, int x, int y) {
        stones[x][y] = color;

        player(color).yourMoveValidated(true);
        opponent(color).stonePlaced(color, x, y);
        opponent(color).yourTurnBegan();
    }

    @Override
    public void pass(Color color) {
        opponent(color).passed(color);
        opponent(color).yourTurnBegan();
    }

    @Override
    public void proposeScoring(Scoring scoring) {

    }

    @Override
    public void acceptScoring(Scoring scoring) {

    }

    @Override
    public void rejectScoring() {

    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) {
        if (black == null) {
            black = generator;

            black.colorSet(Color.BLACK);
            black.setMovePerformer(this);
        } else {
            white = generator;

            white.colorSet(Color.WHITE);
            black.setMovePerformer(this);
            black.yourTurnBegan();
        }
    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }
}
