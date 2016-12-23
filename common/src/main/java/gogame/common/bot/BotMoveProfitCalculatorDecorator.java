package gogame.common.bot;

import gogame.common.Color;

import java.util.List;

public abstract class BotMoveProfitCalculatorDecorator implements BotMoveProfitCalculator {
    protected BotMoveProfitCalculator decorated;

    public void setDecorated(BotMoveProfitCalculator calculator) {
        decorated = calculator;
    }

    protected abstract double calculate(Color color, int x, int y, List<Color[][]> history, double startingProfit);

    public final double calculateProfit(Color color, int x, int y, List<Color[][]> history, double startingProfit) {
        double profit = calculate(color, x, y, history, startingProfit);

        if (decorated != null) {
            return decorated.calculateProfit(color, x, y, history, profit);
        } else {
            return profit;
        }
    }
}
