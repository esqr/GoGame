package gogame.common.validation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import gogame.common.*;


public class SuicideDecoratorMoveValidatorTest {
    SuicideDecoratorMoveValidator validator;
    private List<Color[][]> history;

    @Before
    public void setUp() throws Exception {
        validator = new SuicideDecoratorMoveValidator();
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
    public void testValidMove() throws Exception {
        history.add(fromString(
                "   B |" +
                "   BW|" +
                "   W |" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.BLACK, 1, 3, history);

        assertEquals(true, result);
    }

    @Test
    public void testValidMoveWithoutLiberty() throws Exception {
        history.add(fromString(
                " BBW |" +
                "  WWW|" +
                "  BWB|" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 1, 3, history);

        assertEquals(true, result);
    }

    @Test
    public void testBetweenSameColor() throws Exception {
        history.add(fromString(
                "   BW|" +
                "   WW|" +
                "  BWW|" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 1, 4, history);

        assertEquals(true, result);
    }

    @Test
    public void testIntoEye() throws Exception {
        history.add(fromString(
                "   W |" +
                "  WBW|" +
                "  WWW|" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.BLACK, 1, 3, history);

        assertEquals(false, result);
    }

    @Test
    public void testEdgeEye() throws Exception {
        history.add(fromString(
                "   BB|" +
                "  BBW|" +
                "    B|" +
                "     |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.WHITE, 1, 4, history);

        assertEquals(false, result);
    }

    @Test
    public void testMurderSuicide() throws Exception {
        history.add(fromString(
                " WWWW|" +
                " WBBW|" +
                " WBW |" +
                "  W  |" +
                "     |"
        ));

        boolean result = validator.followsRule(Color.BLACK, 1, 3, history);

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