package gogame.common.validation;

import java.io.IOException;
import java.util.List;
import gogame.common.*;

public class InsideBoardDecoratorMoveValidator extends DecoratorMoveValidator {

    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        int width;
        int height;

        try {
            width = history.get(history.size()-1).length;
            height = history.get(history.size()-1)[0].length;
        } catch (NullPointerException ex) {
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        if (0 <= x && x < width && 0 <= y && y < height) {
            return true;
        }

        return false;
    }
}
