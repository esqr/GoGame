package gogame.client.controllers;

import gogame.client.screenmanager.NoSuchScreenException;
import gogame.client.screenmanager.ScreenManager;
import gogame.client.screenmanager.Screens;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;

public class MainWindowController {
    @FXML
    private ScreenManager screenManager;

    public void setMainScreen(URL resource) throws IOException, NoSuchScreenException {
        screenManager.loadScreen(Screens.MAIN, resource);
        screenManager.setScreen(Screens.MAIN);
    }
}
