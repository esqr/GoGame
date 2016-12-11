package gogame.client.controllers;

import gogame.client.screenmanager.ControlledScreen;
import gogame.client.ui.BoardCanvas;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController extends ControlledScreen {
    @FXML
    private Pane canvasWrapper;

    @FXML
    private BoardCanvas boardCanvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardCanvas.heightProperty().bind(canvasWrapper.heightProperty());
        boardCanvas.widthProperty().bind(canvasWrapper.widthProperty());
    }
}
