package gogame.client.screenmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager extends StackPane {
    private Map<String, Node> screens = new HashMap<>();

    public ScreenManager() {
        super();
    }

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public void loadScreen(String name, String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
        Parent loadScreen = loader.load();
        addScreen(name, loadScreen);
    }

    public void setScreen(String name) throws NoSuchScreenException {
        if (screens.get(name) == null) {
            throw new NoSuchScreenException();
        }

        if (!getChildren().isEmpty()) {
            getChildren().remove(0);
        }

        getChildren().add(screens.get(name));
    }
}
