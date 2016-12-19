package gogame.common.validation;

import java.util.List;
import gogame.common.*;

public interface MoveValidator {
    boolean isMoveValid(Color color, int x, int y, List<Color[][]> history);
}