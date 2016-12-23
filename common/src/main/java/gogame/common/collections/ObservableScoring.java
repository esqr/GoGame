package gogame.common.collections;

import java.util.Arrays;
import java.util.Observable;

public class ObservableScoring extends Observable {
    boolean alive[][];
    int size;

    public ObservableScoring(int size) {
        setSize(size);
    }

    public void setSize(int size) {
        this.size = size;
        alive = new boolean[size][size];
        for (boolean[] row : alive) {
            Arrays.fill(row, false);
        }

        setChanged();
        notifyObservers();
    }

    public int getSize() {
        return size;
    }

    public void setAlive(boolean isAlive, int x, int y) {
        alive[x][y] = isAlive;
        setChanged();
        notifyObservers();
    }

    public boolean getAlive(int x, int y) {
        return alive[x][y];
    }

    public void clear() {
        for (boolean[] row : alive) {
            Arrays.fill(row, false);
        }

        setChanged();
        notifyObservers();
    }
}
