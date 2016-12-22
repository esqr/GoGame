package gogame.client;

import gogame.common.Color;
import gogame.common.MovePerformer;
import gogame.common.Scoring;
import gogame.common.Stone;
import gogame.common.collections.ObservableBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BoardClientTest {
    private BoardClient boardClient = null;
    private ObservableBoard board = null;
    private MovePerformer performer = null;

    @Before
    public void setUp() throws Exception {
        performer = mock(MovePerformer.class);
        board = new ObservableBoard(2);
        boardClient = new BoardClient(performer, board);
    }

    @Test
    public void testPlaceStone() throws Exception {
        Stone stone = new Stone(0, 1, Color.BLACK);
        boardClient.placeStone(stone.getColor(), stone.getPosX(), stone.getPosY());
        verify(performer, times(1)).placeStone(stone.getColor(), stone.getPosX(), stone.getPosY());
    }

    @Test
    public void testPlaceStoneOnOtherStone() throws Exception {
        Stone stone = new Stone(0, 1, Color.BLACK);
        board.setStone(stone.getColor(), stone.getPosX(), stone.getPosY());

        Stone stone2 = new Stone(stone.getPosX(), stone.getPosY(), Color.WHITE);
        boardClient.placeStone(stone2.getColor(), stone.getPosX(), stone.getPosY());

        verify(performer, times(0)).placeStone(stone2.getColor(), stone2.getPosX(), stone2.getPosY());
    }

    @Test
    public void pass() throws Exception {
        boardClient.pass(Color.BLACK);
        verify(performer, times(1)).pass(Color.BLACK);
    }

    @Test
    public void proposeScoring() throws Exception {
        Scoring scoring = mock(Scoring.class);

        boardClient.proposeScoring(scoring);
        verify(performer, times(1)).proposeScoring(scoring);
    }

    @Test
    public void acceptScoring() throws Exception {
        Scoring scoring = mock(Scoring.class);

        boardClient.acceptScoring(scoring);
        verify(performer, times(1)).acceptScoring(scoring);
    }

    @Test
    public void rejectScoring(Color color) throws Exception {
        boardClient.rejectScoring(color);
        verify(performer, times(1)).rejectScoring(color);
    }

}