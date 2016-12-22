package gogame.client;

import gogame.client.statemanager.StateManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

public class RoomListUpdater extends Thread {
    private ObservableList<RoomListElement> roomList;
    private NetClient netClient;

    public RoomListUpdater(ObservableList<RoomListElement> roomList, NetClient netClient) {
        this.roomList = roomList;
        this.netClient = netClient;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (StateManager.INSTANCE.getState() == StateManager.ClientState.ROOM_VIEW) {
                    netClient.requestBoardList();
                }

                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRoomList(List<RoomListElement> roomList) {
        Platform.runLater(() -> {
            this.roomList.clear();
            this.roomList.addAll(roomList);
        });
    }
}
