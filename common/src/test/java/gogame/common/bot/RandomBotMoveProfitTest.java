package gogame.common.bot;

import gogame.common.Color;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RandomBotMoveProfitTest {
    private List<Color[][]> history = null;

    @Before
    public void setUp() throws Exception {
        history = new ArrayList<>();
    }

    @Test
    public void testRange() throws Exception {
        byte[] aLittleLargerThanOne = ByteBuffer.allocate(8).putDouble(1.0).array();
        aLittleLargerThanOne[aLittleLargerThanOne.length - 1]++;

        RandomBotMoveProfit calculator = new RandomBotMoveProfit(1.0, ByteBuffer.wrap(aLittleLargerThanOne).getDouble());
        double profit = calculator.calculate(Color.BLACK, 1, 2, history, 1.0);
        assertTrue(1.0 == profit);
    }
}