package gogame.common;

import java.util.ArrayList;
import java.util.List;

public class Board implements MovePerformer {
    private List<Color[][]> history = new ArrayList<Color[][]>();
    private MoveGenerator black = null;
    private MoveGenerator white = null;

    public Board(int size) {
        Color[][] empty = new Color[size][size];

        for(int i=0;i<empty.length;++i) {
            for (int j = 0; j < empty[0].length; j++) {
                empty[i][j] = Color.NONE;
            }
        }

        history.add(empty);
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
        Color[][] after = new Color[history.get(0).length][history.get(0)[0].length];

        for(int i=0;i<history.get(0).length;++i) {
            for (int j=0; j<history.get(0)[0].length; j++) {
                after[i][j] = history.get(0)[i][j];
            }
        }

        after = BoardTransformer.transform(x, y, after);

        player(color).yourMoveValidated(true);
        history.add(after);

        for(int i=0;i<history.get(0).length;++i) {
            for (int j=0; j<history.get(0)[0].length; j++) {
                if (after[i][j] != history.get(0)[i][j]) {
                    player(color).stonePlaced(color, x, y);
                    opponent(color).stonePlaced(color, x, y);
                }
            }
        }
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
