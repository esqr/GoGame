package gogame.common.validation;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DecoratorMoveValidatorFactoryTest {
    @Test
    public void testCreation() throws Exception {
        // ensure validator classes exist:
        // test will fail at compilation rather than
        // execution when classes disappear
        MoveValidator ib = new InsideBoardDecoratorMoveValidator();
        MoveValidator k = new KoDecoratorMoveValidator();
        MoveValidator s = new SuicideDecoratorMoveValidator();
        // now create it with factory
        List<String> partsNames = Arrays.asList("InsideBoard", "Ko", "Suicide");
        MoveValidator validator = DecoratorMoveValidatorFactory.create(partsNames);

        assertNotEquals(null, validator);
    }

    @Test
    public void testError() throws Exception {
        List<String> partsNames = Arrays.asList("DummyNonExisting");
        MoveValidator validator = DecoratorMoveValidatorFactory.create(partsNames);

        assertEquals(null, validator);
    }
}