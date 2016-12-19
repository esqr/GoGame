package gogame.common.validation;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gogame.common.*;

import static org.junit.Assert.*;

public class NotSupersedesDecoratorMoveValidatorTest {
    private MoveValidator validator;
    private List history;
    Color[][] state;

    @Before
    public void setUp() throws Exception {
        validator = new NotSupersedesDecoratorMoveValidator();
        history = new ArrayList();
        state = new Color[19][19];

        for (int i=0; i<state.length; ++i) {
            for(int j=0;j<state[i].length;++j) {
                state[i][j] = Color.NONE;
            }
        }

        history.add(state);
    }

    @Test
    public void testEmpty() throws Exception {
        boolean result = validator.isMoveValid(Color.BLACK, 0, 0, history);

        assertEquals(true, result);
    }

    @Test
    public void testSameColor() throws Exception {
        state[0][0] = Color.BLACK;

        history.add(state);

        boolean result = validator.isMoveValid(Color.BLACK, 0, 0, history);

        assertEquals(false, result);
    }

    @Test
    public void testDifferentColor() throws Exception {
        state[0][0] = Color.WHITE;

        history.add(state);

        boolean result = validator.isMoveValid(Color.BLACK, 0, 0, history);

        assertEquals(false, result);
    }
}