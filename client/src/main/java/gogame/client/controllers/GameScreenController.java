package gogame.client.controllers;

import gogame.client.BeautyGuiInterface;
import gogame.client.BoardClient;
import gogame.client.screenmanager.ControlledScreen;
import gogame.client.ui.BoardView;
import gogame.common.Color;
import gogame.common.MoveGenerator;
import gogame.common.MovePerformer;
import gogame.common.Scoring;
import gogame.common.collections.ObservableBoard;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController extends ControlledScreen {
    private BeautyGuiInterface beautyGuiInterface;
    private MoveGenerator forwardee;
    private BoardClient boardClient;
    private ObservableBoard board;

    @FXML
    private Pane canvasWrapper;

    @FXML
    private BoardView boardView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardView.heightProperty().bind(canvasWrapper.heightProperty());
        boardView.widthProperty().bind(canvasWrapper.widthProperty());

        forwardee = new MoveGenerator() {
            @Override
            public void colorSet(Color color) {

            }

            @Override
            public void yourTurnBegan() {

            }

            @Override
            public void yourMoveValidated(boolean valid) {

            }

            @Override
            public void stonePlaced(Color color, int x, int y) {
                board.setStone(color, x, y);
            }

            @Override
            public void passed(Color color) {

            }

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
            public void setMovePerformer(MovePerformer performer) {

            }
        };
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
}
