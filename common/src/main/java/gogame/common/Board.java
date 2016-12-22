package gogame.common;

import gogame.common.validation.DecoratorMoveValidatorFactory;
import gogame.common.validation.MoveValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements MovePerformer {
    private List<Color[][]> history = new ArrayList<Color[][]>();
    private MoveGenerator black = null;
    private MoveGenerator white = null;
    private MoveGenerator currentPlayer = null;
    private int size;
    private Room room;
    private MoveValidator moveValidator;

    public Board(int size) {
        this.size = size;
        Color[][] empty = new Color[size][size];

        for(int i=0;i<empty.length;++i) {
            for (int j = 0; j < empty[0].length; j++) {
                empty[i][j] = Color.NONE;
            }
        }

        List<String> partsNames = Arrays.asList("InsideBoard", "Ko", "Suicide");
        moveValidator = DecoratorMoveValidatorFactory.create(partsNames);

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
        if (currentPlayer == player(color)) {
//            boolean validMove = moveValidator.isMoveValid(color, x, y, history);
            boolean validMove = true;

            if (validMove) {
                Color[][] after = new Color[history.get(0).length][history.get(0)[0].length];

                for (int i = 0; i < history.get(0).length; ++i) {
                    for (int j = 0; j < history.get(0)[0].length; j++) {
                        after[i][j] = history.get(0)[i][j];
                    }
                }

                after = BoardTransformer.transform(x, y, after);

                for (int i = 0; i < history.get(0).length; ++i) {
                    for (int j = 0; j < history.get(0)[0].length; j++) {
                        if (after[i][j] != history.get(0)[i][j]) {
                            player(color).stonePlaced(color, x, y);
                            opponent(color).stonePlaced(color, x, y);
                        }
                    }
                }

                history.add(0, after);

                opponent(color).yourTurnBegan();
                currentPlayer = opponent(color);
            }

            player(color).yourMoveValidated(validMove);
        }
    }

    @Override
    public void pass(Color color) {
        if (currentPlayer == player(color)) {
            opponent(color).passed(color);
            opponent(color).yourTurnBegan();
            currentPlayer = opponent(color);
        }
    }

    @Override
    public void proposeScoring(Scoring scoring) {

    }

    @Override
    public void acceptScoring(Scoring scoring) {

    }

    @Override
    public void rejectScoring(Color color) {

    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) throws BoardFullException {
        if (black == null) {
            black = generator;

            black.colorSet(Color.BLACK);
            black.setMovePerformer(this);
        } else if (white == null) {
            white = generator;

            white.colorSet(Color.WHITE);
            black.setMovePerformer(this);
            black.yourTurnBegan();
            currentPlayer = black;
        } else {
            throw new BoardFullException();
        }
    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }

    public int getSize() {
        return size;
    }

    public int getPlayerNumber() {
        int amount = 0;

        if (black != null) {
            amount++;
        }

        if (white != null) {
            amount++;
        }

        return amount;
    }

    public void clientDisconnected(Color color) {
        if (color == Color.BLACK) {
            black = null;
        } else {
            white = null;
        }

        MoveGenerator moveGenerator = opponent(color);

        if (moveGenerator != null) {
            moveGenerator.opponentDisconnected();
        }

        GameServer.getInstance().removeRoom(room);
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
