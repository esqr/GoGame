package gogame.common.collections;

import gogame.common.Color;
import gogame.common.Stone;

import java.util.Arrays;
import java.util.Observable;

public class ObservableBoard extends Observable {
    Color stones[][];
    int size;

    public ObservableBoard(int size) {
        setSize(size);
    }

    public void setSize(int size) {
        this.size = size;
        stones = new Color[size][size];
        for (Color[] row : stones) {
            Arrays.fill(row, Color.NONE);
        }

        setChanged();
        notifyObservers();
    }

    public int getSize() {
        return size;
    }

    public void setStone(Color color, int x, int y) {
        stones[x][y] = color;
        setChanged();
        notifyObservers(new Stone(x, y, color));
    }

    public Color getStone(int x, int y) {
        return stones[x][y];
    }

    public Color[][] asArray() {
        return stones;
    }
}
