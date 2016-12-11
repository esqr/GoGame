package gogame.client.screenmanager;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScreenManagerTest {
    private ScreenManager screenManager = null;
    private static final String screenName = "someScreenName";

    @Before
    public void beforeEachTest() {
        screenManager = new ScreenManager();
    }

    @Test
    public void addGetScreen() throws Exception {
        Node node = new Pane();
        screenManager.addScreen(screenName, node);
        assertEquals(node, screenManager.getScreen(screenName));
    }

    @Test
    public void loadScreen() throws Exception {
        screenManager.loadScreen(screenName, getClass().getClassLoader().getResource("Blank.fxml"));
        assertNotEquals(null, screenManager.getScreen(screenName));
    }

    @Test
    public void setScreen() throws Exception {
        Node node = new Pane();
        screenManager.addScreen(screenName, node);
        screenManager.setScreen(screenName);
        assertEquals(node, screenManager.getChildren().get(0));

        Node node2 = new Pane();
        screenManager.addScreen("node2", node2);
        screenManager.setScreen("node2");
        assertEquals(node2, screenManager.getChildren().get(0));
    }

    @Test(expected = NoSuchScreenException.class)
    public void setInvalidScreen() throws NoSuchScreenException {
        screenManager.setScreen(screenName);
    }

}