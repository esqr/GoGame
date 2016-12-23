package gogame.common.collections;

import gogame.common.Color;
import gogame.common.Stone;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ObservableBoardTest {
    private ObservableBoard board = null;

    @Before
    public void setUp() {
        board = new ObservableBoard(0);
    }

    @Test
    public void testSize() throws Exception {
        int size = 19;
        board.setSize(size);
        assertEquals(size, board.getSize());
    }


    @Test
    public void testStone() throws Exception {
        board.setSize(2);
        Stone stone = new Stone(0, 1, Color.BLACK);
        board.setStone(stone.getColor(), stone.getPosX(), stone.getPosY());
        assertEquals(stone.getColor(), board.getStone(stone.getPosX(), stone.getPosY()));
    }

    @Test
    public void testAsArray() throws Exception {
        Color[][] expectedArray = new Color[2][2];
        for (Color[] row : expectedArray) {
            Arrays.fill(row, Color.NONE);
        }

        board.setSize(2);
        assertArrayEquals(expectedArray, board.asArray());
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testNegativeSize() throws Exception {
        board.setSize(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundsStone() throws Exception {
        board.setStone(Color.BLACK, 2, 0);
    }
}