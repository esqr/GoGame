package gogame.common.bot;

import java.util.Stack;

public class BotMoveProfitCalculatorBuilder {
    private Stack<BotMoveProfitCalculatorDecorator> decorators;

    public BotMoveProfitCalculatorBuilder() {
        decorators = new Stack<>();
    }

    public BotMoveProfitCalculatorBuilder addDecorator(BotMoveProfitCalculatorDecorator decorator) {
        decorators.push(decorator);
        return this;
    }

    public BotMoveProfitCalculator getDecoratedCalculator() {
        BotMoveProfitCalculator calculator = null;

        while (!decorators.isEmpty()) {
            BotMoveProfitCalculatorDecorator decorator = decorators.pop();
            decorator.setDecorated(calculator);
            calculator = decorator;
        }

        return calculator;
    }

    public static BotMoveProfitCalculator getDefaultCalculator() {
        BotMoveProfitCalculatorBuilder builder = new BotMoveProfitCalculatorBuilder();
        builder.addDecorator(new RandomBotMoveProfit(0.5, 1.5));
        return builder.getDecoratedCalculator();
    }
}
