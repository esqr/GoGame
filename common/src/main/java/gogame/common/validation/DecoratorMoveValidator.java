package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public abstract class DecoratorMoveValidator implements MoveValidator {
    private MoveValidator decorated = null;

    public void setDecoratedValidator(MoveValidator validator) {
        decorated = validator;
    }

    protected abstract boolean followsRule(Color color, int x, int y, List<Color[][]> history);

    @Override
    public final boolean isMoveValid(Color color, int x, int y, List<Color[][]> history) {
        if (!followsRule(color, x, y, history)) {
            return false;
        } else if (decorated != null) {
            return decorated.isMoveValid(color, x, y, history);
        } else {
            return true;
        }
    }
}
