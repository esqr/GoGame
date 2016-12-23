package gogame.common.bot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BotMoveProfitCalculatorBuilderTest {
    BotMoveProfitCalculatorBuilder builder;

    @Before
    public void setUp() {
        builder = new BotMoveProfitCalculatorBuilder();
    }

    @Test
    public void testBuild() throws Exception {
        BotMoveProfitCalculatorDecorator decorator = mock(BotMoveProfitCalculatorDecorator.class);
        builder.addDecorator(decorator);

        assertNotNull(builder.getDecoratedCalculator());
    }

    @Test
    public void testNoDecoratorBuild() throws Exception {
        assertNull(builder.getDecoratedCalculator());
    }
}