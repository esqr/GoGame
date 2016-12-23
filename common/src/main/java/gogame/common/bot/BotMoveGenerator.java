package gogame.common.bot;

import gogame.common.Color;
import gogame.common.MoveGenerator;
import gogame.common.MovePerformer;
import gogame.common.Scoring;
import gogame.common.validation.DecoratorMoveValidatorFactory;
import gogame.common.validation.MoveValidator;

import java.util.List;

public class BotMoveGenerator implements MoveGenerator {
    private Color color;
    private List<Color[][]> history;
    private BotMoveProfitCalculator moveProfitCalculator = BotMoveProfitCalculatorBuilder.getDefaultCalculator();
    private MoveValidator validator = DecoratorMoveValidatorFactory.create();
    private MovePerformer performer;

    public BotMoveGenerator() {

    }

    @Override
    public void colorSet(Color color) {
        this.color = color;
    }

    @Override
    public void yourTurnBegan() {
        Color[][] currentState = history.get(history.size() - 1);

        // int and double must be initialized to compare them
        int x = -1;
        int y = -1;
        double profit = -1.0;

        double probability = 1 / currentState.length / currentState[0].length;

        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[0].length; j++) {
                if (currentState[i][j] == Color.NONE) {
                    double newProfit = moveProfitCalculator.calculateProfit(color, i, j, history, 1.0);
                    if ((newProfit > profit || (newProfit == profit && Math.random() < probability))
                            && validator.isMoveValid(color, i, j, history)) {
                        profit = newProfit;
                        x = i;
                        y = j;
                    }
                }
            }
        }

        if (profit >= 1.0) {
            performer.placeStone(color, x, y);
        } else {
            performer.pass(color);
        }
    }

    @Override
    public void yourMoveValidated(boolean valid) {
        if (!valid) {
            // this should never happen
            performer.pass(color);
        }
    }

    @Override
    public void stonePlaced(Color color, int x, int y) {

    }

    @Override
    public void passed(Color color) {

    }

    @Override
    public void scoringProposed(Scoring scoring) {
        // accept everything
        performer.acceptScoring(scoring);
    }

    @Override
    public void scoringAccepted(Scoring scoring) {

    }

    @Override
    public void scoringRejected() {

    }

    @Override
    public void setMovePerformer(MovePerformer performer) {
        this.performer = performer;
    }

    @Override
    public void opponentDisconnected() {

    }

    public void setHistory(List<Color[][]> history) {
        this.history = history;
    }
}
