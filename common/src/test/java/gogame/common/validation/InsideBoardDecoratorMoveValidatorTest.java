package gogame.common.validation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import gogame.common.*;

public class InsideBoardDecoratorMoveValidatorTest {
    private InsideBoardDecoratorMoveValidator validator;
    private List history;
    Color[][] state;
    private static final int size = 19;

    @Before
    public void setUp() throws Exception {
        validator = new InsideBoardDecoratorMoveValidator();
        history = new ArrayList();
        state = new Color[size][size];

        for (int i=0; i<state.length; ++i) {
            for(int j=0;j<state[i].length;++j) {
                state[i][j] = Color.NONE;
            }
        }

        history.add(state);
    }

    @Test
    public void testFirstAngle() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, 0, 0, history);

        assertEquals(true, result);
    }

    @Test
    public void testLastAngle() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, size-1, size-1, history);

        assertEquals(true, result);
    }

    @Test
    public void testCenter() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, size/2, size/2, history);

        assertEquals(true, result);
    }

    @Test
    public void testTooLeft() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, -2, size/2, history);

        assertEquals(false, result);
    }

    @Test
    public void testTooRightBottom() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, size, 2*size, history);

        assertEquals(false, result);
    }

    @Test
    public void testNullHistory() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, 0, 0, null);

        assertEquals(true, result);
    }

    @Test
    public void testEmptyHistory() throws Exception {
        boolean result = validator.followsRule(Color.BLACK, 0, 0, new ArrayList<Color[][]>());

        assertEquals(true, result);
    }

    @Test
    public void testNullContainingHistory() throws Exception {
        ArrayList<Color[][]> history = new ArrayList<Color[][]>();
        history.add(null);

        boolean result = validator.followsRule(Color.BLACK, 0, 0, history);

        assertEquals(true, result);
    }
}