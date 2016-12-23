package gogame.common;

import gogame.common.bot.BotMoveGenerator;
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
    private MoveValidator validator = DecoratorMoveValidatorFactory.create();
    private Scoring scoring;
    private boolean scoringMode = false;
    private MoveGenerator scoringAccepted = null;
    private boolean passState = false;

    public Board(int size) {
        this.size = size;
        Color[][] empty = new Color[size][size];

        for(int i=0;i<empty.length;++i) {
            for (int j = 0; j < empty[0].length; j++) {
                empty[i][j] = Color.NONE;
            }
        }

        history.add(empty);

        scoring = new Scoring();
        scoring.alive = new Boolean[size][size];
        for (Boolean[] row : scoring.alive) {
            Arrays.fill(row, false);
        }
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
        if (!scoringMode && currentPlayer == player(color)) {
            passState = false;
            boolean validMove = validator.isMoveValid(color, x, y, history);

            if (validMove) {
                Color[][] lastState = history.get(history.size()-1);
                Color[][] after = new Color[lastState.length][lastState[0].length];
                for(int i = 0;i < lastState.length; ++i) {
                    for (int j = 0; j < lastState[0].length; j++) {
                        after[i][j] = lastState[i][j];
                    }
                }

                after[x][y] = color;
                after = BoardTransformer.transform(x, y, after);

                for (int i = 0; i < lastState.length; ++i) {
                    for (int j = 0; j < lastState[0].length; j++) {
                        if (after[i][j] != lastState[i][j]) {
                            player(color).stonePlaced(after[i][j], i, j);
                            opponent(color).stonePlaced(after[i][j], i, j);
                        }
                    }
                }

                history.add(after);
                currentPlayer = opponent(color);
                opponent(color).yourTurnBegan();
            }

            player(color).yourMoveValidated(validMove);
        }
    }

    @Override
    public void pass(Color color) {
        if (!scoringMode && currentPlayer == player(color)) {
            if (passState) {
                scoringMode = true;
                passState = false;
                opponent(color).scoringStarted();
                player(color).scoringStarted();
            } else {
                passState = true;
                currentPlayer = opponent(color);
                opponent(color).passed(color);
                opponent(color).yourTurnBegan();
            }
        }
    }

    @Override
    public void acceptScoring(Color color) {
        if (scoringMode) {
            if (scoringAccepted == opponent(color)) {
                // both players accepted scoring
                black.scoringAccepted();
                white.scoringAccepted();
            } else {
                scoringAccepted = player(color);
            }
        }
    }

    @Override
    public void rejectScoring(Color color) {
        if (scoringMode) {
            scoringMode = false;
            scoringAccepted = null;
            currentPlayer = opponent(color);
            currentPlayer.scoringRejected();
            currentPlayer.yourTurnBegan();
        }
    }

    @Override
    public void proposeAlive(List<Stone> alive) {
        if (scoringMode) {
            scoringAccepted = null;

            for (Stone stone : alive) {
                scoring.alive[stone.getPosX()][stone.getPosY()] = true;
            }

            black.aliveProposed(alive);
            white.aliveProposed(alive);
        }
    }

    @Override
    public void proposeDead(List<Stone> dead) {
        if (scoringMode) {
            scoringAccepted = null;

            for (Stone stone : dead) {
                scoring.alive[stone.getPosX()][stone.getPosY()] = false;
            }

            black.deadProposed(dead);
            white.deadProposed(dead);
        }
    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) throws BoardFullException {
        if (black == null) {
            black = generator;

            black.colorSet(Color.BLACK);
        } else if (white == null) {
            white = generator;

            white.colorSet(Color.WHITE);

            if (generator instanceof BotMoveGenerator) {
                ((BotMoveGenerator) generator).setHistory(history);
            }

            currentPlayer = black;
            black.yourTurnBegan();
        } else {
            throw new BoardFullException();
        }
    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void surrender(Color color) {
        if (color == Color.BLACK) {
            black = null;
        } else {
            white = null;
        }

        MoveGenerator moveGenerator = opponent(color);

        if (moveGenerator != null) {
            moveGenerator.opponentSurrendered();
        }

        GameServer.getInstance().removeRoom(room);
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
