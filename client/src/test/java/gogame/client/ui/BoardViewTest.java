package gogame.client.ui;

import gogame.common.collections.ObservableBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardViewTest {
    private BoardView boardView = null;

    @Before
    public void setUp() throws Exception {
        boardView = new BoardView();
    }

    @Test
    public void testBoardSize() throws Exception {
        assertEquals(0, boardView.getBoardSize());
    }

    @Test
    public void testSetBoard() throws Exception {
        boardView.setBoard(new ObservableBoard(19));
        assertEquals(19, boardView.getBoardSize());
    }

}