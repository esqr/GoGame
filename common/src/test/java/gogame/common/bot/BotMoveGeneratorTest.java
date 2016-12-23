package gogame.common.bot;

import gogame.common.Color;
import gogame.common.MovePerformer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BotMoveGeneratorTest {
    private BotMoveGenerator generator = null;
    private MovePerformer performer = null;
    private List<Color[][]> history = null;

    @Before
    public void setUp() throws Exception {
        Color[][] empty = new Color[2][2];
        for (Color[] row : empty) {
            Arrays.fill(row, Color.NONE);
        }
        history = new ArrayList<>();
        history.add(empty);

        generator = new BotMoveGenerator();
        performer = mock(MovePerformer.class);
        generator.colorSet(Color.BLACK);
        generator.setMovePerformer(performer);
        generator.setHistory(history);
    }

    @Test
    public void yourTurnBegan() throws Exception {
        final int[] i = {0};

        Answer incrementCounter = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                i[0]++;
                return null;
            }
        };

        doAnswer(incrementCounter).when(performer).placeStone(eq(Color.BLACK), anyInt(), anyInt());
        doAnswer(incrementCounter).when(performer).pass(eq(Color.BLACK));

        generator.yourTurnBegan();

        assertTrue(i[0] == 1);
    }

    @Test
    public void testPass() throws Exception {
        Color[][] full = new Color[2][2];
        for (Color[] row : full) {
            Arrays.fill(row, Color.BLACK);
        }
        history.add(full);

        generator.yourTurnBegan();

        verify(performer, times(1)).pass(Color.BLACK);
    }

    @Test
    public void yourMoveValidated() throws Exception {
        generator.yourMoveValidated(false);
        verify(performer, times(1)).pass(eq(Color.BLACK));
    }

    @Test
    public void aliveProposed() throws Exception {
        generator.aliveProposed(new ArrayList<>());
        verify(performer, times(1)).acceptScoring(Color.BLACK);
    }

    @Test
    public void deadProposed() throws Exception {
        generator.deadProposed(new ArrayList<>());
        verify(performer, times(1)).acceptScoring(Color.BLACK);
    }

    @Test
    public void testIgnored() throws Exception {
        generator.stonePlaced(null, 0, 0);
        generator.passed(null);
        generator.scoringAccepted(null);
        generator.scoringRejected();
        generator.opponentDisconnected();
        generator.opponentSurrendered();
        generator.scoringStarted();

        verifyZeroInteractions(performer);
    }
}