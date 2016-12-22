package gogame.client.controllers;

import gogame.client.screenmanager.*;
import gogame.client.statemanager.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private ScreenManager screenManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StateManager.INSTANCE.setScreenManager(screenManager);
    }

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
