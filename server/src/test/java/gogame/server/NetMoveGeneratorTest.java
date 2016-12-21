package gogame.server;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetMoveGeneratorTest {
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
}