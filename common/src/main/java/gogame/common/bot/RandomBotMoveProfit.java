package gogame.common.bot;

import gogame.common.Color;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBotMoveProfit extends BotMoveProfitCalculatorDecorator {
    private double multiplierMin;
    private double multiplierMax;

    public RandomBotMoveProfit(double multiplierMin, double multiplierMax) {
        this.multiplierMin = multiplierMin;
        this.multiplierMax = multiplierMax;
    }

    @Override
    public double calculate(Color color, int x, int y, List<Color[][]> history, double startingProfit) {
        double multiplier = ThreadLocalRandom.current().nextDouble(multiplierMin, multiplierMax);
        return startingProfit * multiplier;
    }
}
