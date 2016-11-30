package gogame.server;

import java.net.ServerSocket;

public class Server {
    private static final int PORT = 8484;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);

        try {
            while (true) {
                new Player(serverSocket.accept());
            }
        } finally {
            serverSocket.close();
        }
    }
}
