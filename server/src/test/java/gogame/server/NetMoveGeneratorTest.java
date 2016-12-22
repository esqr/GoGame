package gogame.server;

import gogame.common.Board;
import gogame.common.Color;
import gogame.common.CommunicationConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class NetMoveGeneratorTest {
    private PipedOutputStream pos = null;
    private PipedInputStream is = null;
    private ByteArrayOutputStream os = null;
    private NetMoveGenerator netMoveGenerator = null;
    private Board movePerformer = null;


    @Before
    public void setUp() throws Exception {
        pos = new PipedOutputStream();
        is = new PipedInputStream(pos);
        os = new ByteArrayOutputStream();
        movePerformer = mock(Board.class);
        netMoveGenerator = new NetMoveGenerator(new PrintWriter(os, true),
                new BufferedReader(new InputStreamReader(is)));
        netMoveGenerator.setMovePerformer(movePerformer);
    }

    @Test
    public void testCommands() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.STONE_PLACED + " 1 1\n").getBytes());
        pos.write((CommunicationConstants.PASS + "\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());

        thread.join();

        verify(movePerformer, times(1)).placeStone(null, 1, 1);
        verify(movePerformer, times(1)).pass(null);
        verify(movePerformer, times(1)).clientDisconnected(null);
    }

    @Test
    public void colorSet() throws Exception {
        netMoveGenerator.colorSet(Color.BLACK);
        assertEquals(CommunicationConstants.COLOR_SET + " " + Color.BLACK.toString() + "\n", os.toString());
    }

    @Test
    public void yourTurnBegan() throws Exception {
        netMoveGenerator.yourTurnBegan();
        assertEquals(CommunicationConstants.YOUR_TURN + "\n", os.toString());
    }

    @Test
    public void yourMoveValidated() throws Exception {
        netMoveGenerator.yourMoveValidated(false);
        netMoveGenerator.yourMoveValidated(true);

        assertEquals(CommunicationConstants.MOVE_VALIDATED + " " + CommunicationConstants.WRONG + "\n" +
                        CommunicationConstants.MOVE_VALIDATED + " " + CommunicationConstants.OK + "\n",
                os.toString());
    }

    @Test
    public void stonePlaced() throws Exception {
        netMoveGenerator.stonePlaced(Color.BLACK, 1, 2);
        assertEquals(CommunicationConstants.STONE_PLACED + " " + Color.BLACK + " " + Integer.toString(1) + " " + Integer.toString(2) + "\n",
                os.toString());
    }

    @Test
    public void passed() throws Exception {
        netMoveGenerator.passed(Color.BLACK);
        assertEquals(CommunicationConstants.PASSED + " " + Color.BLACK + "\n",
                os.toString());
    }

    @Test
    public void opponentDisconnected() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        netMoveGenerator.opponentDisconnected();
        thread.join();
        assertEquals(CommunicationConstants.OPPONENT_DISCONNECTED + "\n",
                os.toString());
        verify(movePerformer, times(1)).clientDisconnected(null);
    }

    @Test
    public void getBoard() throws Exception {
        assertSame(movePerformer, netMoveGenerator.getBoard());
    }

    /*
    @Test(timeout = 2000)
    public void testNetMoveGenerator() throws Exception {
        Socket socket = mock(Socket.class);
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(pos);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);

        NetMoveGenerator player = new NetMoveGenerator(socket);
        assertEquals("HELLO\n", os.toString());
        assertTrue(player.isAlive());
        pos.write("BYE\n".getBytes());
        player.join();
        assertFalse(player.isAlive());
    }

    @Test
    public void testBadSocket() throws Exception {
        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenThrow(new IOException());
        when(socket.getInputStream()).thenThrow(new IOException());

        NetMoveGenerator player = new NetMoveGenerator(socket);
        assertFalse(player.isAlive());
    }
    */
}