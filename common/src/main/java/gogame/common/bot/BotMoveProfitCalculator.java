package gogame.common.bot;

import gogame.common.Color;

import java.util.List;

public interface BotMoveProfitCalculator {
    double calculateProfit(Color color, int x, int y, List<Color[][]> history, double startingProfit);
}
