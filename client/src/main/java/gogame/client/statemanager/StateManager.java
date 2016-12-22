package gogame.client.statemanager;

import gogame.client.screenmanager.NoSuchScreenException;
import gogame.client.screenmanager.ScreenManager;
import gogame.client.screenmanager.Screens;
import javafx.application.Platform;

public enum StateManager {
    INSTANCE;

    public enum ClientState {
        ROOM_VIEW,
        IN_GAME
    }

    ClientState currentState;
    ScreenManager screenManager;

    public void setState(ClientState state) throws NoSuchScreenException, IllegalStateChangeException {
        switch (state) {
            case ROOM_VIEW:
                Platform.runLater(() -> {
                    try {
                        screenManager.setScreen(Screens.GAME_LIST);
                    } catch (NoSuchScreenException e) {
                        e.printStackTrace();
                    }
                });
                break;

            case IN_GAME:
                if (currentState == ClientState.ROOM_VIEW) {
                    Platform.runLater(() -> {
                        try {
                            screenManager.setScreen(Screens.GAME);
                        } catch (NoSuchScreenException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    throw new IllegalStateChangeException();
                }

                break;
        }

        currentState = state;
    }

    public ClientState getState() {
        return currentState;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
}
