package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public class KoDecoratorMoveValidator extends DecoratorMoveValidator {
    @Override
    protected boolean followsRule(Color color, int x, int y, List<Color[][]> history) {
        Color[][] transformed;
        Color[][] previous;

        try {
            previous = BoardTransformer.transform(x, y, history.get(history.size()-3));
            transformed = BoardTransformer.transform(x, y, history.get(history.size()-1));
        } catch (NullPointerException ex) {
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        for (int i=0;i<transformed.length;++i) {
            for (int j=0;j<transformed[0].length;++j) {
                if (transformed[i][j] != previous[i][j]) {
                    return true;
                }
            }
        }

        return false;
    }
}
