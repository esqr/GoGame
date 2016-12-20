package gogame.common.validation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import gogame.common.*;


public class KoDecoratorMoveValidatorTest {
    KoDecoratorMoveValidator validator;
    private List<Color[][]> history;

    @Before
    public void setUp() throws Exception {
        validator = new KoDecoratorMoveValidator();
        history = new ArrayList<Color[][]>();
    }

    @Test
    public void testFirstMove() throws Exception {
        history.add(fromString(
                "     |" +
                "    W|" +
                "     |" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 1, 4, history);

        assertEquals(true, result);
    }

    @Test
    public void testSecondMove() throws Exception {
        history.add(fromString(
                "   B |" +
                "     |" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "   B |" +
                "    W|" +
                "     |" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.BLACK, 0, 3, history);

        assertEquals(true, result);
    }

    @Test
    public void testValidMove() throws Exception {
        history.add(fromString(
                "    B|" +
                "     |" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "    W|" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   W |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   WB|" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 2, 4, history);

        assertEquals(true, result);
    }

    @Test
    public void testValidMove2() throws Exception {
        history.add(fromString(
                "    B|" +
                "     |" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "    W|" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "     |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   W |" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   WB|" +
                "     |" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   B |" +
                "   WB|" +
                "    W|" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 3, 4, history);

        assertEquals(true, result);
    }

    @Test
    public void testKo() throws Exception {
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   W |" +
                "    W|" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   B |" +
                "   WB|" +
                "    W|" +
                "     |"
        ));
        history.add(fromString(
                "    B|" +
                "   BW|" +
                "   WB|" +
                "    W|" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 1, 4, history);

        assertEquals(false, result);
    }

    private Color[][] fromString(String state) {
        int size = state.indexOf('|');

        Color[][] result = new Color[size][size];

        for (int i=0;i<size; ++i) {
            for (int j=0;j<size; ++j) {
                switch (state.charAt(i*(size+1) + j)) {
                    case 'B':
                        result[i][j] = Color.BLACK;
                        break;
                    case 'W':
                        result[i][j] = Color.WHITE;
                        break;
                    case ' ':
                        result[i][j] = Color.NONE;
                        break;
                }
            }
        }

        return result;
    }
}
