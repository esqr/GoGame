package gogame.client;

import java.net.Socket;
import gogame.common.MovePerformer;

public class ClientApplication {
    private static final int PORT = 8484;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", PORT);
        MovePerformer performer = new NetClient(socket);
    }
}
