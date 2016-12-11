package gogame.client.controllers;

import gogame.client.screenmanager.ControlledScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameListScreenController extends ControlledScreen {
    @FXML
    private ListView gameListView;

    @FXML
    private TextField newGameTextField;

    @FXML
    private Button newGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newGameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onNewGame();
            }
        });
        newGameButton.setOnMouseClicked(event -> onNewGame());
    }

    private void onNewGame() {
        // todo
    }
}
