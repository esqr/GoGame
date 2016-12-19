package gogame.client.controllers;

import gogame.client.screenmanager.*;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;

public class MainWindowController {
    @FXML
    private ScreenManager screenManager;

    public void setMainScreen(URL resource) throws IOException, NoSuchScreenException, NoControllerException {
        screenManager.loadScreen(Screens.MAIN, resource);
        screenManager.setScreen(Screens.MAIN);
    }

    public void addScreen(String name, URL resource) throws IOException, NoControllerException {
        screenManager.loadScreen(name, resource);
    }

    public ControlledScreen getScreen(String name) {
        return screenManager.getScreen(name);
    }
}
