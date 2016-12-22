package gogame.client;

import gogame.client.controllers.RoomListScreenController;
import gogame.client.controllers.GameScreenController;
import gogame.client.controllers.MainWindowController;
import gogame.client.screenmanager.Screens;
import gogame.client.statemanager.StateManager;
import gogame.common.collections.ObservableBoard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.Socket;

public class ClientApplication extends Application {
    private static final int PORT = 8484;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ThreadExceptionHandler exceptionHandler = (e) -> {
            showError(e, false);
        };

        NetClient netClient = null;
        BeautyGuiInterface generator;

        try {
            Socket socket = new Socket("127.0.0.1", PORT);
            netClient = new NetClient(socket, exceptionHandler);
        } catch (Exception e) {
            e.printStackTrace();
            showError(e, true);
        }

        generator = new BeautyGuiInterface();
        netClient.setGenerator(generator);
        netClient.setDaemon(true);
        netClient.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        MainWindowController controller = new MainWindowController();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();

        controller.setMainScreen(getClass().getClassLoader().getResource("screens/RoomListScreen.fxml"));
        controller.addScreen(Screens.GAME, getClass().getClassLoader().getResource("screens/GameScreen.fxml"));
        StateManager.INSTANCE.setState(StateManager.ClientState.ROOM_VIEW);

        GameScreenController gameScreenController = ((GameScreenController) controller.getScreen(Screens.GAME));
        gameScreenController.setBeautyGuiInterface(generator);

        ObservableBoard board = new ObservableBoard(0);
        BoardClient boardClient = new BoardClient(netClient, board);
        netClient.setBoardClient(boardClient);
        gameScreenController.setBoard(board);
        gameScreenController.setBoardClient(boardClient);

        ObservableList<RoomListElement> roomList = FXCollections.observableArrayList();
        RoomListScreenController roomListScreenController = (RoomListScreenController) controller.getScreen(Screens.GAME_LIST);
        roomListScreenController.setNetClient(netClient);
        roomListScreenController.setRoomList(roomList);
        RoomListUpdater roomListUpdater = new RoomListUpdater(roomList, netClient);
        netClient.setRoomListUpdater(roomListUpdater);
        roomListUpdater.setDaemon(true);
        roomListUpdater.start();


        primaryStage.setTitle("Gra Go");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void showError(Exception e, boolean exit) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();

            if (exit) {
                System.exit(1);
            }
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();

            if (exit) {
                System.exit(1);
            }
        });
    }
}
