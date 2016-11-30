package gogame.server;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {
    @Test(timeout = 2000)
    public void testPlayer() throws Exception {
        Socket socket = mock(Socket.class);
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(pos);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);

        Player player = new Player(socket);
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

        Player player = new Player(socket);
        assertFalse(player.isAlive());
    }

    @Test(timeout = 2000)
    public void testReadError() throws Exception {
        Socket socket = mock(Socket.class);
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(pos);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);

        Player player = new Player(socket);
        assertTrue(player.isAlive());

        player.reader = mock(BufferedReader.class);
        when(player.reader.readLine()).thenThrow(new IOException());
        pos.write("\n".getBytes());
        player.join();
        assertFalse(player.isAlive());
    }

    @Test(timeout = 2000)
    public void testSocketCloseError() throws Exception {
        Socket socket = mock(Socket.class);
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(pos);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);

        Player player = new Player(socket);
        assertTrue(player.isAlive());

        player.reader = mock(BufferedReader.class);
        when(player.reader.readLine()).thenReturn(null);
        doThrow(new IOException()).when(player.socket).close();
        pos.write("\n".getBytes());
        player.join();
        assertFalse(player.isAlive());
    }
}