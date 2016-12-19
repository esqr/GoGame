package gogame.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTransformerTest {
    @Test(timeout=1000)
    public void testAngleInsert() throws Exception {
        Color[][] state = fromString(
                "B    |" +
                "     |" +
                "     |" +
                "     |" +
                "     |"
        );
        String before = toString(state);
        Color[][] nextState = BoardTransformer.transform(0, 0, state);

        assertEquals(before, toString(nextState));
    }

    @Test(timeout=1000)
    public void testBetweenSameColor() throws Exception {
        Color[][] state = fromString(
                "  B  |" +
                " BBB |" +
                "  B  |" +
                "     |" +
                "     |"
        );
        String before = toString(state);
        Color[][] nextState = BoardTransformer.transform(1, 2, state);

        assertEquals(before, toString(nextState));
    }

    @Test(timeout=1000)
    public void testBetweenOtherColor() throws Exception {
        Color[][] state = fromString(
                "  B  |" +
                " BWB |" +
                " WB  |" +
                "     |" +
                "     |"
        );
        Color[][] expectedState = fromString(
                "  B  |" +
                " B B |" +
                " WB  |" +
                "     |" +
                "     |"
        );
        Color[][] nextState = BoardTransformer.transform(1, 2, state);

        assertEquals(toString(expectedState), toString(nextState));
    }

    @Test(timeout=1000)
    public void testCapture() throws Exception {
        Color[][] state = fromString(
                "  BB |" +
                " BWWB|" +
                " WBB |" +
                "     |" +
                "     |"
        );
        Color[][] expectedState = fromString(
                "  BB |" +
                " B  B|" +
                " WBB |" +
                "     |" +
                "     |"
        );
        Color[][] nextState = BoardTransformer.transform(2, 2, state);

        assertEquals(toString(expectedState), toString(nextState));
    }

    @Test(timeout=1000)
    public void testEyeCapture() throws Exception {
        Color[][] state = fromString(
                "       |" +
                "WWWW   |" +
                "WBBBW  |" +
                "WBWBW  |" +
                "WBBBW  |" +
                "WWWWW  |" +
                "       |"
        );
        Color[][] expectedState = fromString(
                "       |" +
                "WWWW   |" +
                "W   W  |" +
                "W W W  |" +
                "W   W  |" +
                "WWWWW  |" +
                "       |"
        );
        Color[][] nextState = BoardTransformer.transform(3, 2, state);

        assertEquals(toString(expectedState), toString(nextState));
    }

    @Test(timeout=1000)
    public void testCaptureOnEdge() throws Exception {
        Color[][] state = fromString(
                "   BB|" +
                "  BWW|" +
                " WWBW|" +
                "    B|" +
                "     |"
        );
        Color[][] expectedState = fromString(
                "   BB|" +
                "  B  |" +
                " WWB |" +
                "    B|" +
                "     |"
        );
        Color[][] nextState = BoardTransformer.transform(0, 4, state);

        assertEquals(toString(expectedState), toString(nextState));
    }

    @Test(timeout=1000)
    public void testKo() throws Exception {
        Color[][] state = fromString(
                " W   |" +
                "WBW  |" +
                "BWB  |" +
                " B   |" +
                "     |"
        );
        Color[][] expectedState = fromString(
                " W   |" +
                "WBW  |" +
                "B B  |" +
                " B   |" +
                "     |"
        );
        Color[][] nextState = BoardTransformer.transform(1, 1, state);

        assertEquals(toString(expectedState), toString(nextState));
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

    private String toString(Color[][] state) {
        String result = "";

        for (int i=0;i<state.length; ++i) {
            for (int j=0;j<state[0].length; ++j) {
                switch (state[i][j]) {
                    case BLACK:
                        result = result + 'B';
                        break;
                    case WHITE:
                        result = result + 'W';
                        break;
                    case NONE:
                        result = result + ' ';
                        break;
                }
            }
            result = result + '|';
        }

        return result;
    }
}