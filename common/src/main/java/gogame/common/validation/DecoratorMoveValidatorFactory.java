package gogame.common.validation;

import java.util.List;

import gogame.common.*;

public class DecoratorMoveValidatorFactory {
    public static DecoratorMoveValidator create(List<String> rules) {
        DecoratorMoveValidator result = null;

        for (int i = rules.size() - 1; i >= 0; i--) {
            DecoratorMoveValidator part;

            try {
                Class c = Class.forName("gogame.common.validation." + rules.get(i) + "DecoratorMoveValidator");
                part = (DecoratorMoveValidator) c.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                return null;
            }

            if (result != null) {
                part.setDecoratedValidator(result);
            }

            result = part;
        }

        return result;
    }
}
