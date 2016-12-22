package gogame.client;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RoomListElement {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty size;
    private final SimpleIntegerProperty playerNumber;

    public RoomListElement(String name, int size, int playerNumber) {
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleIntegerProperty(size);
        this.playerNumber = new SimpleIntegerProperty(playerNumber);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getSize() {
        return size.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public int getPlayerNumber() {
        return playerNumber.get();
    }

    public SimpleIntegerProperty playerNumberProperty() {
        return playerNumber;
    }
}
