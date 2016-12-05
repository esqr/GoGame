package gogame.client;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetClientTest {
    @Test(timeout = 20000)
    public void testClientWriting() throws Exception {
        Socket socket = mock(Socket.class);
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(pos);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(os);
        when(socket.getInputStream()).thenReturn(is);

        NetClient player = new NetClient(socket);
        assertEquals("HELLO\nBYE\n", os.toString());
    }
}
