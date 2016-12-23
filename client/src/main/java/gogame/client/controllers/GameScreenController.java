package gogame.client.controllers;

import gogame.client.BeautyGuiInterface;
import gogame.client.BoardClient;
import gogame.client.ClientApplication;
import gogame.client.screenmanager.ControlledScreen;
import gogame.client.screenmanager.NoSuchScreenException;
import gogame.client.statemanager.IllegalStateChangeException;
import gogame.client.statemanager.StateManager;
import gogame.client.ui.BoardView;
import gogame.common.*;
import gogame.common.collections.ObservableBoard;
import gogame.common.collections.ObservableScoring;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameScreenController extends ControlledScreen {
    private BeautyGuiInterface beautyGuiInterface;
    private MoveGenerator forwardee;
    private BoardClient boardClient;
    private ObservableBoard board;
    private volatile boolean moved = false;
    private StateStrategy stateStrategy;
    private ObservableScoring scoring = new ObservableScoring(0);
    private Color selfColor;

    @FXML
    private Pane canvasWrapper;

    @FXML
    private BoardView boardView;

    @FXML
    private Label statusLabel;

    @FXML
    private Button passButton;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button acceptScoringButton;

    @FXML
    private Button rejectScoringButton;


    // strategy pattern
    private interface StateStrategy {
        void onBoardClick(MouseEvent event);
    }

    private class NormalState implements StateStrategy {
        @Override
        public void onBoardClick(MouseEvent event) {
            Pair<Integer, Integer> pos = boardView.calcPointerPosition(event.getX(), event.getY());
            if (pos != null) {
                moved = true;
                boardClient.placeStone(null, pos.getKey(), pos.getValue());
            }
        }
    }

    private class ScoringState implements StateStrategy {
        @Override
        public void onBoardClick(MouseEvent event) {
            Pair<Integer, Integer> pos = boardView.calcPointerPosition(event.getX(), event.getY());
            if (pos != null) {
                List<Stone> stoneChain = Utils.getStoneChain(pos.getKey(), pos.getValue(), board.asArray());

                if (event.getButton() == MouseButton.PRIMARY) {
                    boardClient.proposeAlive(stoneChain);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    boardClient.proposeDead(stoneChain);
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardView.heightProperty().bind(canvasWrapper.heightProperty());
        boardView.widthProperty().bind(canvasWrapper.widthProperty());
        boardView.setScoring(scoring);

        forwardee = new MoveGenerator() {
            @Override
            public void colorSet(Color color) {
                Platform.runLater(() -> {
                    selfColor = color;
                    stateStrategy = new NormalState();
                    setDisableMove(true);
                    statusLabel.setText("Twój kolor: " + color);
                });
            }

            @Override
            public void yourTurnBegan() {
                Platform.runLater(() -> {
                    moved = false;
                    setDisableMove(false);
                    statusLabel.setText("Twoja tura");
                });
            }

            @Override
            public void yourMoveValidated(boolean valid) {
                Platform.runLater(() -> {
                    if (moved) {
                        if (valid) {
                            statusLabel.setText("Ruch przeciwnika");
                            setDisableMove(true);
                        } else {
                            statusLabel.setText("Nieprawidłowy ruch");
                        }
                    }
                });
            }

            @Override
            public void stonePlaced(Color color, int x, int y) {
                Platform.runLater(() -> {
                    board.setStone(color, x, y);
                });
            }

            @Override
            public void passed(Color color) {}

            @Override
            public void scoringAccepted(Scoring sc) {
                Platform.runLater(() -> {
                    scoring.clear();
                    setDisableScoringButtons(true);
                    stateStrategy = new NormalState();
                    if (sc.winner == selfColor) {
                        ClientApplication.showInfo("Wygrałeś", "Gratulacje! Wygrałeś");
                    } else {
                        ClientApplication.showInfo("Przegrałeś", "Może następnym razem...");
                    }

                    try {
                        StateManager.INSTANCE.setState(StateManager.ClientState.ROOM_VIEW);
                    } catch (NoSuchScreenException | IllegalStateChangeException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void scoringRejected() {
                Platform.runLater(() -> {
                    scoring.clear();
                    setDisableScoringButtons(true);
                    stateStrategy = new NormalState();
                });
            }

            @Override
            public void setMovePerformer(MovePerformer performer) {}

            @Override
            public void opponentDisconnected() {}

            @Override
            public void opponentSurrendered() {}

            @Override
            public void scoringStarted() {
                Platform.runLater(() -> {
                    statusLabel.setText("Zaznacz żywe grupy: LPM - oznacz jako żywą, PPM - oznacz jako martwą");
                    setDisableScoringButtons(false);
                    stateStrategy = new ScoringState();
                    passButton.setDisable(true);
                    boardView.setDisable(false);
                });
            }

            @Override
            public void aliveProposed(List<Stone> alive) {
                Platform.runLater(() -> {
                    setDisableScoringButtons(false);
                    for (Stone stone : alive) {
                        scoring.setAlive(true, stone.getPosX(), stone.getPosY());
                    }
                });
            }

            @Override
            public void deadProposed(List<Stone> dead) {
                Platform.runLater(() -> {
                    setDisableScoringButtons(false);
                    for (Stone stone : dead) {
                        scoring.setAlive(false, stone.getPosX(), stone.getPosY());
                    }
                });
            }
        };

        // actually all Color parameters can be null (at client side)
        // that is because it's even not sent to server

        boardView.setOnMouseClicked(event -> {
            stateStrategy.onBoardClick(event);
        });

        passButton.setOnMouseClicked(event -> {
            boardClient.pass(null);
            moved = true;
            setDisableMove(true);
        });

        surrenderButton.setOnMouseClicked(event -> {
            boardClient.surrender(null);
            try {
                StateManager.INSTANCE.setState(StateManager.ClientState.ROOM_VIEW);
            } catch (NoSuchScreenException | IllegalStateChangeException e) {
                e.printStackTrace();
            }
        });

        acceptScoringButton.setOnMouseClicked(event -> {
            boardClient.acceptScoring(null);
            setDisableScoringButtons(true);
        });

        rejectScoringButton.setOnMouseClicked(event -> {
            statusLabel.setText("Ruch przeciwnika");
            setDisableMove(true);

            scoring.clear();
            setDisableScoringButtons(true);
            stateStrategy = new NormalState();

            boardClient.rejectScoring(null);
        });
    }

    public void setBeautyGuiInterface(BeautyGuiInterface beautyGuiInterface) {
        this.beautyGuiInterface = beautyGuiInterface;
        this.beautyGuiInterface.setForwardee(forwardee);
    }

    public void setBoardClient(BoardClient boardClient) {
        this.boardClient = boardClient;
    }

    public void setBoard(ObservableBoard board) {
        this.board = board;
        boardView.setBoard(board);
        board.addObserver((o, arg) -> {
            if (board.getSize() != scoring.getSize()) {
                scoring.setSize(board.getSize());
            }
        });
    }

    private void setDisableMove(boolean value) {
        boardView.setDisable(value);
        passButton.setDisable(value);
    }

    private void setDisableScoringButtons(boolean value) {
        acceptScoringButton.setDisable(value);
        rejectScoringButton.setDisable(value);
    }
}
