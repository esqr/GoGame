package gogame.client.controllers;

import gogame.client.screenmanager.ControlledScreen;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class GameListScreenController extends ControlledScreen {
    private ObservableList<Integer> boardList;

    @FXML
    private ListView<Integer> gameListView;

    @FXML
    private TextField newGameTextField;

    @FXML
    private Button newGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameListView.setItems(boardList);

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

    public void setBoardList(ObservableList<Integer> boardList) {
        this.boardList = boardList;
    }
}
