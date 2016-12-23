package gogame.common.bot;

import gogame.common.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    public void testDefaultCalculator() throws Exception {
        BotMoveProfitCalculator calculator = BotMoveProfitCalculatorBuilder.getDefaultCalculator();
        double profit = calculator.calculateProfit(Color.BLACK, 1, 2, new ArrayList<Color[][]>(), 0.0);
        assertTrue(0.0 == profit);
    }

    @Test
    public void testNoDecoratorBuild() throws Exception {
        assertNull(builder.getDecoratedCalculator());
    }
}