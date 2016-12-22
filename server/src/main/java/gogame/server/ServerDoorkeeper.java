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
        SimpleLogger.log("client connected");
        try {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                SimpleLogger.log("recv command: ", inputLine);
                Scanner scanner = new Scanner(inputLine);
                String command = scanner.next();
                if (command.equals(CommunicationConstants.HELLO)) {
                    writer.println(CommunicationConstants.HELLO);
                } else if (command.equals(CommunicationConstants.GET_BOARDS)) {
                    StringBuilder sb = new StringBuilder();
                    String filterString = scanner.next();
                    GameServer.Filter filter = GameServer.Filter.valueOf(filterString);

                    sb.append(CommunicationConstants.BOARD_LIST);
                    List<Room> rooms = GameServer.getInstance().getRoomList(filter);

                    for (Room room : rooms) {
                        sb.append(" ").append(room.getName())
                                .append(" ").append(room.getBoard().getSize())
                                .append(" ").append(room.getBoard().getPlayerNumber());
                    }

                    writer.println(sb.toString());
                } else if (command.equals(CommunicationConstants.JOIN_BOARD)) {
                    String roomName = scanner.next();
                    NetMoveGenerator generator = new NetMoveGenerator(writer, reader);

                    try {
                        GameServer.getInstance().addPlayerToBoard(generator, roomName);
                        writer.println(CommunicationConstants.JOINED + " " + generator.getBoard().getSize());
                        generator.start();
                    } catch (BoardFullException e) {
                        writer.println(CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_FULL);
                    }

                } else if (command.equals(CommunicationConstants.NEW_BOARD)) {
                    String roomName = scanner.next();
                    int boardSize = scanner.nextInt();
                    try {
                        GameServer.getInstance().newBoard(roomName, boardSize);
                        NetMoveGenerator generator = new NetMoveGenerator(writer, reader);

                        GameServer.getInstance().addPlayerToBoard(generator, roomName);
                        writer.println(CommunicationConstants.JOINED + " " + generator.getBoard().getSize());
                        generator.start();
                    } catch (BoardExistsException e) {
                        writer.println(CommunicationConstants.ERROR + " " + CommunicationConstants.Errors.BOARD_EXISTS);
                    } catch (BoardFullException shouldNeverHappen) {
                        shouldNeverHappen.printStackTrace();
                    }
                } else if (inputLine.equals(CommunicationConstants.BYE)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SimpleLogger.log("client disconnected");
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}