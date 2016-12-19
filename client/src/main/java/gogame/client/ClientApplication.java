package gogame.client;

import gogame.client.controllers.GameListScreenController;
import gogame.client.controllers.GameScreenController;
import gogame.client.controllers.MainWindowController;
import gogame.client.screenmanager.Screens;
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
        NetClient netClient = null;
        BeautyGuiInterface generator;

        try {
            Socket socket = new Socket("127.0.0.1", PORT);
            netClient = new NetClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
            ClientApplication.connectionError(e, true);
        }

        generator = new BeautyGuiInterface();
        netClient.setGenerator(generator);
        netClient.setDaemon(true);
        netClient.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        MainWindowController controller = new MainWindowController();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();

        controller.setMainScreen(getClass().getClassLoader().getResource("screens/GameListScreen.fxml"));
        controller.addScreen(Screens.GAME, getClass().getClassLoader().getResource("screens/GameScreen.fxml"));

        GameScreenController gameScreenController = ((GameScreenController) controller.getScreen(Screens.GAME));
        gameScreenController.setBeautyGuiInterface(generator);

        ObservableBoard board = new ObservableBoard(19);
        BoardClient boardClient = new BoardClient(netClient, board);
        gameScreenController.setBoard(board);
        gameScreenController.setBoardClient(boardClient);

        ObservableList<Integer> boardList = FXCollections.observableArrayList();
        ((GameListScreenController) controller.getScreen(Screens.GAME_LIST)).setBoardList(boardList);
        BoardListUpdater boardListUpdater = new BoardListUpdater(boardList, netClient);
        netClient.setBoardListUpdater(boardListUpdater);
        boardListUpdater.setDaemon(true);
        boardListUpdater.start();


        primaryStage.setTitle("Gra Go");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void connectionError(Exception e, boolean exit) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd połączenia");
        alert.setHeaderText("Błąd połączenia");
        alert.setContentText(e.getLocalizedMessage());
        if (Platform.isFxApplicationThread()) {
            alert.showAndWait();

            if (exit) {
                System.exit(1);
            }
        }

        Platform.runLater(() -> {
            alert.showAndWait();

            if (exit) {
                System.exit(1);
            }
        });
    }
}
