package gogame.common.validation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import gogame.common.*;

public class InsideBoardDecoratorMoveValidatorTest {
    private MoveValidator validator;
    private List history;
    Color[][] state;
    private static final int size = 19;

    @Before
    public void setUp() throws Exception {
        validator = new NotSupersedesDecoratorMoveValidator();
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
        DecoratorMoveValidator validator = new InsideBoardDecoratorMoveValidator();

        boolean result = validator.followsRule(Color.BLACK, 0, 0, history);

        assertEquals(true, result);
    }

    @Test
    public void testLastAngle() throws Exception {
        DecoratorMoveValidator validator = new InsideBoardDecoratorMoveValidator();

        boolean result = validator.followsRule(Color.BLACK, size-1, size-1, history);

        assertEquals(true, result);
    }

    @Test
    public void testCenter() throws Exception {
        DecoratorMoveValidator validator = new InsideBoardDecoratorMoveValidator();

        boolean result = validator.followsRule(Color.BLACK, size/2, size/2, history);

        assertEquals(true, result);
    }

    @Test
    public void testTooLeft() throws Exception {
        DecoratorMoveValidator validator = new InsideBoardDecoratorMoveValidator();

        boolean result = validator.followsRule(Color.BLACK, -2, size/2, history);

        assertEquals(false, result);
    }

    @Test
    public void testTooRightBottom() throws Exception {
        DecoratorMoveValidator validator = new InsideBoardDecoratorMoveValidator();

        boolean result = validator.followsRule(Color.BLACK, size, 2*size, history);

        assertEquals(false, result);
    }

}