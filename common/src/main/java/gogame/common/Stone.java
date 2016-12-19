package gogame.client.misc;

import gogame.common.Color;

public class Stone {
    private int posX;
    private int posY;
    private Color color;

    public Stone(int x, int y, Color color) {
        posX = x;
        posY = y;
        this.color = color;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Color getColor() {
        return color;
    }
}
