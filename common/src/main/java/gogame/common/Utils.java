package gogame.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Utils {
    public static int[][] getNeighbours(int[] position) {
            return new int[][]{
                    {position[0]-1, position[1]},
                    {position[0]+1, position[1]},
                    {position[0], position[1]-1},
                    {position[0], position[1]+1}
            };
    }

    public static List<int[]> getChain(int startX, int startY, Color[][] state) {
        boolean[][] visited = new boolean[state.length][state[0].length];
        List<int[]> sameChain = new ArrayList<int[]>();
        Queue<int[]> queue = new LinkedList<int[]>();
        int[] startPosition = {startX, startY};

        visited[startX][startY] = true;
        sameChain.add(startPosition);
        queue.add(startPosition);

        while (!queue.isEmpty()) {
            int[] actualPosition = queue.remove();

            for (int[] adjacent: getNeighbours(actualPosition)) {
                int visitedX = adjacent[0];
                int visitedY = adjacent[1];

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

    public static List<Stone> getStoneChain(int startx, int starty, Color[][] state) {
        List<int[]> chain = getChain(startx, starty, state);
        List<Stone> stoneChain = new ArrayList<>();

        for (int[] chainElement : chain) {
            Stone stone = new Stone(chainElement[0], chainElement[1], state[chainElement[0]][chainElement[1]]);
            stoneChain.add(stone);
        }

        return stoneChain;
    }
}
