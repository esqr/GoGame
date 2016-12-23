package gogame.client.controllers;

import gogame.client.BeautyGuiInterface;
import gogame.client.BoardClient;
import gogame.client.screenmanager.ControlledScreen;
import gogame.client.screenmanager.NoSuchScreenException;
import gogame.client.statemanager.IllegalStateChangeException;
import gogame.client.statemanager.StateManager;
import gogame.client.ui.BoardView;
import gogame.common.Color;
import gogame.common.MoveGenerator;
import gogame.common.MovePerformer;
import gogame.common.Scoring;
import gogame.common.collections.ObservableBoard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController extends ControlledScreen {
    private BeautyGuiInterface beautyGuiInterface;
    private MoveGenerator forwardee;
    private BoardClient boardClient;
    private ObservableBoard board;
    private volatile boolean moved = false;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardView.heightProperty().bind(canvasWrapper.heightProperty());
        boardView.widthProperty().bind(canvasWrapper.widthProperty());

        forwardee = new MoveGenerator() {
            @Override
            public void colorSet(Color color) {
                Platform.runLater(() -> {
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
            public void scoringProposed(Scoring scoring) {

            }

            @Override
            public void scoringAccepted(Scoring scoring) {

            }

            @Override
            public void scoringRejected() {

            }

            @Override
            public void setMovePerformer(MovePerformer performer) {}

            @Override
            public void opponentDisconnected() {}

            @Override
            public void opponentSurrendered() {}
        };

        // actually all Color parameters can be null (at client side)
        // that is because it's even not sent to server

        boardView.setOnMouseClicked(event -> {
            Pair<Integer, Integer> pos = boardView.calcPointerPosition(event.getX(), event.getY());
            if (pos != null) {
                moved = true;
                boardClient.placeStone(null, pos.getKey(), pos.getValue());
            }
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
    }

    private void setDisableMove(boolean value) {
        boardView.setDisable(value);
        passButton.setDisable(value);
    }
}
