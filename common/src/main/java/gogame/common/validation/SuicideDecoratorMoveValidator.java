package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class SuicideDecoratorMoveValidator extends DecoratorMoveValidator {
    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        Color[][] state = history.get(history.size()-1);
        Color[][] transformed = new Color[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                transformed[i][j] = state[i][j];
            }
        }

        transformed[x][y] = color;

        try {
            transformed = BoardTransformer.transform(x, y, transformed);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        return transformed[x][y] != Color.NONE;
    }
}
