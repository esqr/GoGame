package gogame.client.controllers;

import gogame.client.screenmanager.ControlledScreen;
import gogame.client.ui.BoardView;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController extends ControlledScreen {
    @FXML
    private Pane canvasWrapper;

    @FXML
    private BoardView boardView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardView.heightProperty().bind(canvasWrapper.heightProperty());
        boardView.widthProperty().bind(canvasWrapper.widthProperty());
    }
}
