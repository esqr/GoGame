package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class NotSupersedesDecoratorMoveValidator extends DecoratorMoveValidator {
    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        Color actualColor;

        try {
            actualColor = history.get(0)[x][y];
        } catch (NullPointerException ex) {
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        return actualColor == Color.NONE;
    }
}
