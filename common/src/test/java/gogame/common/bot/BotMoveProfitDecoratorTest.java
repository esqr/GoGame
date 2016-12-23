package gogame.common.bot;

import gogame.common.Color;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class BotMoveProfitDecoratorTest {
    @Test
    public void testChain() throws Exception {
        BotMoveProfitCalculatorBuilder  builder = new BotMoveProfitCalculatorBuilder();
        builder.addDecorator(new RandomBotMoveProfit(2.0, 2.1)).addDecorator(new RandomBotMoveProfit(2.0, 2.1));
        BotMoveProfitCalculator calculator = builder.getDecoratedCalculator();

        double profit = calculator.calculateProfit(Color.BLACK, 1, 1, new ArrayList<>(), 1.0);
        assertTrue(profit > 4 && profit < 4.5);
    }
}
