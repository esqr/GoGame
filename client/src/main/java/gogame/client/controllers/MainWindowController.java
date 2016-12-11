package gogame.client.controllers;

import gogame.client.screenmanager.NoSuchScreenException;
import gogame.client.screenmanager.ScreenManager;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainWindowController {
    @FXML
    private ScreenManager screenManager;

    public void setMainScreen(String resourceName) throws IOException, NoSuchScreenException {
        screenManager.loadScreen("main", resourceName);
        screenManager.setScreen("main");
    }
}
