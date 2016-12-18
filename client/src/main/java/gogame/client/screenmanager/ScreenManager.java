package gogame.client.screenmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager extends StackPane {
    private Map<String, ControlledScreen> screens = new HashMap<>();
    private ControlledScreen currentScreen;

    public ScreenManager() {
        super();
    }

    public void addScreen(String name, ControlledScreen screen) {
        screens.put(name, screen);
    }

    public ControlledScreen getScreen(String name) {
        return screens.get(name);
    }

    public void loadScreen(String name, URL resource) throws IOException, NoControllerException {
        FXMLLoader loader = new FXMLLoader(resource);
        Parent loadScreen = loader.load();
        ControlledScreen controller = loader.getController();

        if (controller == null) {
            throw new NoControllerException();
        }

        controller.screenManager = this;
        controller.self = loadScreen;

        addScreen(name, controller);
    }

    public void setScreen(String name) throws NoSuchScreenException {
        if (screens.get(name) == null) {
            throw new NoSuchScreenException();
        }

        if (!getChildren().isEmpty()) {
            getChildren().remove(0);
        }



        currentScreen = screens.get(name);
        getChildren().add(currentScreen.self);
    }

    public ControlledScreen getCurrentScreen() {
        return currentScreen;
    }
}
