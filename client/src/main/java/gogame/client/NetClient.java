package gogame.client;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetClient extends Thread implements MovePerformer {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private MoveGenerator generator;
    private BoardListUpdater boardListUpdater;

    NetClient(Socket socket) throws Exception {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;

        out.println("HELLO");

        if (!in.readLine().equals("HELLO")) {
            throw new ProtocolException("Błąd protokołu");
        }
    }

    @Override
    public void run() {
        try {
            String line;

            while ((line = in.readLine()) != null) {
                Scanner scanner = new Scanner(line);
                String command = scanner.next();

                if (command.equals("STONE")) {
                    Color color = Color.valueOf(scanner.next());
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();

                    generator.stonePlaced(color, x, y);
                } else if (command.equals("PASS")) {
                    Color color = Color.valueOf(scanner.next());

                    generator.passed(color);
                } else if (command.equals("BYE")) {
                    break;
                } else if (command.equals("BOARDS")) {
                    List<Integer> boardList = new ArrayList<>();
                    try {
                        while (true) {
                            boardList.add(scanner.nextInt());
                        }
                    } catch (NoSuchElementException ignored) {}

                    boardListUpdater.setBoardList(boardList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ClientApplication.connectionError(e, true);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                ClientApplication.connectionError(e, true);
            }
        }
    }

    public void requestBoardList() {
        out.println("GET_BOARDS " + GameServer.Filter.ALL.toString());
    }

    public void setGenerator(MoveGenerator generator) {
        this.generator = generator;
    }

    public void setBoardListUpdater(BoardListUpdater boardListUpdater) {
        this.boardListUpdater = boardListUpdater;
    }

    @Override
    public void placeStone(Color color, int x, int y) {
        out.println("STONE " + Integer.toString(x) + " " + Integer.toString(y));
    }

    @Override
    public void pass(Color color) {
        out.println("PASS " + color.toString());
    }

    @Override
    public void proposeScoring(Scoring scoring) {

    }

    @Override
    public void acceptScoring(Scoring scoring) {

    }

    @Override
    public void rejectScoring() {

    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }
}
