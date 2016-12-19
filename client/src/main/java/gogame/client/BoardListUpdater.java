package gogame.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

public class BoardListUpdater extends Thread {
    private ObservableList<Integer> boardList;
    private NetClient netClient;

    public BoardListUpdater(ObservableList<Integer> boardList, NetClient netClient) {
        this.boardList = boardList;
        this.netClient = netClient;
    }

    @Override
    public void run() {
        try {
            while (true) {
                netClient.requestBoardList();
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setBoardList(List<Integer> boardList) {
        Platform.runLater(() -> {
            this.boardList.clear();
            this.boardList.addAll(boardList);
        });
    }
}
