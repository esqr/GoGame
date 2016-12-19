package gogame.server;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ServerDoorkeeper extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ServerDoorkeeper(Socket socket) {
        this.socket = socket;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean socketPassed = false;

        try {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                Scanner scanner = new Scanner(inputLine);
                String command = scanner.next();
                if (command.equals("HELLO")) {
                    writer.println("HELLO");
                } else if (command.equals("GET_BOARDS")) {
                    String filterString = scanner.next();
                    GameServer.Filter filter = GameServer.Filter.valueOf(filterString);

                    String reply = "BOARDS ";
                    List<Integer> boards = GameServer.getInstance().getBoardsList(filter);

                    for (Integer board : boards) {
                        reply = reply + Integer.toString(board);
                    }

                    writer.println(reply);
                } else if (command.equals("JOIN_BOARD")) {
                    int board = scanner.nextInt();
                    MoveGenerator generator = new NetMoveGenerator(socket);
                    socketPassed = true;

                    GameServer.getInstance().addPlayerToBoard(generator, board);

                    break;
                } else if (inputLine.equals("BYE")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                if (!socketPassed) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}