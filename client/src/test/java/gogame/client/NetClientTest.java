package gogame.client;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ProtocolException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetClientTest {
    private Socket socket = null;
    private NetClient netClient = null;
    private PipedOutputStream pos = null;
    private PipedInputStream is = null;
    private ByteArrayOutputStream os = null;

    @Before
    public void beforeEachTest() throws Exception {
        socket = mock(Socket.class);
        pos = new PipedOutputStream();
        is = new PipedInputStream(pos);
        os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);
    }

    @Test(timeout = 1000)
    public void testHello() throws Exception {
        pos.write("HELLO\n".getBytes());
        netClient = new NetClient(socket, null);
        assertEquals("HELLO\n", os.toString());
    }

    @Test(timeout = 1000, expected = ProtocolException.class)
    public void testBadHello() throws Exception {
        pos.write("NOT A HELLO\n".getBytes());
        netClient = new NetClient(socket, null);
    }
}
