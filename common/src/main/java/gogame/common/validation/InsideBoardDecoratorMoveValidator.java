package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class InsideBoardDecoratorMoveValidator extends DecoratorMoveValidator {

    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        if (history == null || history.isEmpty() || history.get(0) == null || (0 <= x && x < history.get(0).length && 0 <= y && y < history.get(0)[0].length)) {
            return true;
        }
        return false;

    }
}
