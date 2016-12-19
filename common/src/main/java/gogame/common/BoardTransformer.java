package gogame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BoardTransformer {
    private static final int[][] NEIGHBOURS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

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
                    List<int[]> chain = getChain(visitedX, visitedY, state);

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

    private static List<int[]> getChain(int startX, int startY, Color[][] state) {
        boolean[][] visited = new boolean[state.length][state[0].length];
        List<int[]> sameChain = new ArrayList<int[]>();
        Queue<int[]> queue = new LinkedList<int[]>();
        int[] startPosition = {startX, startY};

        visited[startX][startY] = true;
        sameChain.add(startPosition);
        queue.add(startPosition);

        while (!queue.isEmpty()) {
            int[] actualPosition = queue.remove();

            for (int[] adjacent: NEIGHBOURS) {
                int visitedX = actualPosition[0]+adjacent[0];
                int visitedY = actualPosition[1]+adjacent[1];

                try {
                    if (!visited[visitedX][visitedY] && state[visitedX][visitedY] == state[startX][startY]) {
                        int[] visitedPosition = {visitedX, visitedY};

                        sameChain.add(visitedPosition);
                        queue.add(visitedPosition);
                    }

                    visited[visitedX][visitedY] = true;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // it's OK
                }
            }
        }

        return sameChain;
    }

    private static boolean hasLiberty(int x, int y, Color[][] state) {
        List<int[]> chain = getChain(x, y, state);
        for (int[] stone : chain) {
            for (int[] adjacent : NEIGHBOURS) {
                try {
                    if (state[stone[0] + adjacent[0]][stone[1] + adjacent[1]] == Color.NONE) {
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
