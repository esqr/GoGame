package gogame.client.controllers;

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
    private Button newGameButton;

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

        newGameButton.setOnMouseClicked(event -> onNewGame());

        roomTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                netClient.joinBoard(roomTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void onNewGame() {
        if (!newGameTextField.getText().isEmpty()) {
            netClient.newBoard(newGameTextField.getText(), 19);
        }
    }

    public void setRoomList(ObservableList<RoomListElement> roomList) {
        this.roomList = roomList;
        roomTableView.setItems(roomList);
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }
}
