package gogame.client.controllers;

import gogame.client.ClientApplication;
import gogame.client.InvalidBoardSizeException;
import gogame.client.NetClient;
import gogame.client.RoomListElement;
import gogame.client.screenmanager.ControlledScreen;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomListScreenController extends ControlledScreen {
    private ObservableList<RoomListElement> roomList;
    private NetClient netClient;

    @FXML
    private TableView<RoomListElement> roomTableView;

    @FXML
    private TextField newGameTextField;

    @FXML
    private TextField boardSizeTextField;

    @FXML
    private Button newGameButton;

    @FXML
    private Button playWithBotButton;

    @FXML
    private TableColumn<RoomListElement, Integer> rtvSizeCol;

    @FXML
    private TableColumn<RoomListElement, Integer> rtvPlayersCol;

    @FXML
    private TableColumn<RoomListElement, String> rtvNameCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rtvNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rtvPlayersCol.setCellValueFactory(new PropertyValueFactory<>("playerNumber"));
        rtvSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        newGameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onNewGame();
            }
        });

        boardSizeTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                boardSizeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }));

        newGameButton.setOnMouseClicked(event -> onNewGame());

        roomTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                netClient.joinBoard(roomTableView.getSelectionModel().getSelectedItem());
            }
        });

        playWithBotButton.setOnMouseClicked(event -> {
            try {
                int boardSize = getBoardSize();
                netClient.playWithBot(boardSize);
            } catch (InvalidBoardSizeException e) {
                ClientApplication.showError(e, false);
            }
        });
    }

    private void onNewGame() {
        if (!newGameTextField.getText().isEmpty()) {
            try {
                int boardSize = getBoardSize();
                netClient.newBoard(newGameTextField.getText(), boardSize);
            } catch (InvalidBoardSizeException e) {
                ClientApplication.showError(e, false);
            }
        }
    }

    public void setRoomList(ObservableList<RoomListElement> roomList) {
        this.roomList = roomList;
        roomTableView.setItems(roomList);
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    private int getBoardSize() throws InvalidBoardSizeException {
        if (boardSizeTextField.getText().isEmpty()) {
            return 19; // default board size
        }

        int boardSize = 0;

        try {
            boardSize = Integer.parseInt(boardSizeTextField.getText());
        } catch (Exception ignored) {}

        // minimum board size (3x3)
        if (boardSize > 2) {
            return boardSize;
        }

        throw new InvalidBoardSizeException("Zły rozmiar planszy");
    }
}
