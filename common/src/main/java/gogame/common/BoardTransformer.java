package gogame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BoardTransformer {

    public static Color[][] transform(int changedX, int changedY, Color[][] state) {
        final int[][] NEIGHBOURS_AND_SELF = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {0, 0}};
        Color[][] resultState = new Color[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                resultState[i][j] = state[i][j];
            }
        }

        for (int[] adjacent: NEIGHBOURS_AND_SELF) {
            try {
                int visitedX = changedX+adjacent[0];
                int visitedY = changedY+adjacent[1];

                if (!hasLiberty(visitedX, visitedY, resultState)) {
                    List<int[]> chain = Utils.getChain(visitedX, visitedY, state);

                    for (int[] stone: chain) {
                        resultState[stone[0]][stone[1]] = Color.NONE;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                // it's OK
            }
        }

        return resultState;
    }

    private static boolean hasLiberty(int x, int y, Color[][] state) {
        List<int[]> chain = Utils.getChain(x, y, state);
        for (int[] stone : chain) {
            for (int[] adjacent : Utils.getNeighbours(stone)) {
                try {
                    if (state[adjacent[0]][adjacent[1]] == Color.NONE) {
                        return true;
                    }

                } catch (ArrayIndexOutOfBoundsException ex) {
                    // it's OK
                }
            }
        }

        return false;
    }
}
