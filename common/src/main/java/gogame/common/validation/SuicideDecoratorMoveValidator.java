package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class SuicideDecoratorMoveValidator extends DecoratorMoveValidator {
    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        Color[][] transformed;

        try {
            transformed = BoardTransformer.transform(x, y, history.get(history.size()-1));
        } catch (NullPointerException ex) {
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        return transformed[x][y] != Color.NONE;
    }
}
