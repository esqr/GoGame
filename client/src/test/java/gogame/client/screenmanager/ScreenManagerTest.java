package gogame.client.screenmanager;

import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ScreenManagerTest {
    private ScreenManager screenManager = null;
    private static final String screenName = "someScreenName";

    @Before
    public void beforeEachTest() {
        screenManager = new ScreenManager();
    }

    @Test
    public void addGetScreen() throws Exception {
        ControlledScreen screen = mock(ControlledScreen.class);

        screenManager.addScreen(screenName, screen);
        assertEquals(screen, screenManager.getScreen(screenName));
    }

    @Test
    public void loadScreen() throws Exception {
        screenManager.loadScreen(screenName, getClass().getClassLoader().getResource("Blank.fxml"));
        assertNotEquals(null, screenManager.getScreen(screenName));
    }

    @Test
    public void setScreen() throws Exception {
        screenManager.loadScreen(screenName, getClass().getClassLoader().getResource("Blank.fxml"));
        screenManager.setScreen(screenName);
        assertTrue(screenManager.getChildren().get(0) instanceof Pane);

    }

    @Test(expected = NoSuchScreenException.class)
    public void setInvalidScreen() throws NoSuchScreenException {
        screenManager.setScreen(screenName);
    }

}