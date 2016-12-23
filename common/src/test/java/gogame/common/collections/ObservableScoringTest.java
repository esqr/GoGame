package gogame.common.collections;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObservableScoringTest {
    private ObservableScoring scoring = null;

    @Before
    public void setUp() {
        scoring = new ObservableScoring(0);
    }

    @Test
    public void testSize() throws Exception {
        int size = 19;
        scoring.setSize(size);
        assertEquals(size, scoring.getSize());
    }

    @Test
    public void testAlive() throws Exception {
        scoring.setSize(2);
        scoring.setAlive(true, 1, 1);
        assertTrue(scoring.getAlive(1, 1));
    }

    @Test
    public void testClear() throws Exception {
        scoring.setSize(2);
        scoring.setAlive(true, 1, 1);
        scoring.clear();
        assertFalse(scoring.getAlive(1, 1));
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testNegativeSize() throws Exception {
        scoring.setSize(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundsStone() throws Exception {
        scoring.setAlive(true, 2, 0);
    }
}