package gogame.client;

import gogame.common.Color;
import gogame.common.MoveGenerator;
import gogame.common.Scoring;
import gogame.common.Stone;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BeautyGuiInterfaceTest {
    private BeautyGuiInterface beautyGuiInterface = null;
    private MoveGenerator forwardee = null;

    @Before
    public void setUp() throws Exception {
        beautyGuiInterface = new BeautyGuiInterface();
        forwardee = mock(MoveGenerator.class);
        beautyGuiInterface.setForwardee(forwardee);
    }

    @Test
    public void colorSet() throws Exception {
        beautyGuiInterface.colorSet(Color.BLACK);
        verify(forwardee, times(1)).colorSet(Color.BLACK);
    }

    @Test
    public void yourTurnBegan() throws Exception {
        beautyGuiInterface.yourTurnBegan();
        verify(forwardee, times(1)).yourTurnBegan();
    }

    @Test
    public void yourMoveValidated() throws Exception {
        beautyGuiInterface.yourMoveValidated(true);
        verify(forwardee, times(1)).yourMoveValidated(true);
    }

    @Test
    public void stonePlaced() throws Exception {
        Stone stone = new Stone(0, 1, Color.BLACK);
        beautyGuiInterface.stonePlaced(stone.getColor(), stone.getPosX(), stone.getPosY());
        verify(forwardee, times(1)).stonePlaced(stone.getColor(), stone.getPosX(), stone.getPosY());
    }

    @Test
    public void passed() throws Exception {
        beautyGuiInterface.passed(Color.BLACK);
        verify(forwardee, times(1)).passed(Color.BLACK);
    }

    @Test
    public void scoringRejected() throws Exception {
        beautyGuiInterface.scoringRejected();
        verify(forwardee, times(1)).scoringRejected();
    }
}