package gogame.common;

public class Scoring {
    /** color of stone on board */
    public Color[][] stones;
    /** true if stone is alive, false if dead or there is no stone */
    public Boolean[][] alive;
    /** BLACK or WHITE if is a part of territory. NONE for alive stone in particular. */
    public Color territory;
    /** which player won */
    public Color winner;
}
