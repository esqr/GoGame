package gogame.client.screenmanager;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class ControlledScreen implements Initializable {
    protected Parent self;
    protected ScreenManager screenManager;
}
