package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class NotSupersedesDecoratorMoveValidator extends DecoratorMoveValidator {
    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        if (history == null || history.isEmpty() || history.get(0) == null || history.get(0)[x][y] == null || history.get(0)[x][y] == Color.NONE) {
            return true;
        }

        return false;
    }
}
