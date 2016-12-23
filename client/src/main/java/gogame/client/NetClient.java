package gogame.client;

import gogame.client.statemanager.StateManager;
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
    private RoomListUpdater roomListUpdater;
    private ThreadExceptionHandler exceptionHandler;
    private BoardClient boardClient;
    private Color color;

    NetClient(Socket socket, ThreadExceptionHandler exceptionHandler) throws Exception {
        this.exceptionHandler = exceptionHandler;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;

        out.println(CommunicationConstants.HELLO);

        if (!in.readLine().equals(CommunicationConstants.HELLO)) {
            throw new ProtocolException("Błąd protokołu");
        }
    }

    @Override
    public void run() {
        try {
            String line;

            while ((line = in.readLine()) != null) {
                SimpleLogger.log(line);
                Scanner scanner = new Scanner(line);
                String command = scanner.next();

                switch (command) {
                    case CommunicationConstants.BOARD_LIST:
                        List<RoomListElement> roomList = new ArrayList<>();

                        try {
                            while (true) {
                                String boardName = scanner.next();
                                int boardSize = scanner.nextInt();
                                int playerNumber = scanner.nextInt();

                                roomList.add(new RoomListElement(boardName, boardSize, playerNumber));
                            }
                        } catch (NoSuchElementException ignored) {}

                        roomListUpdater.setRoomList(roomList);
                        break;

                    case CommunicationConstants.ERROR:
                        String error = scanner.next();
                        try {
                            switch (error) {
                                case CommunicationConstants.Errors.BOARD_FULL:
                                    throw new BoardFullException("Nie można dołączyć: pokój jest pełny");

                                case CommunicationConstants.Errors.BOARD_EXISTS:
                                    throw new BoardExistsException("Pokój o takiej nazwie istnieje");

                                case CommunicationConstants.Errors.BOARD_SIZE:
                                    throw new InvalidBoardSizeException("Zły rozmiar planszy");

                                default:
                                    throw new Exception("Błąd: Błąd (Błąd)"); // revolver ocelot
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            exceptionHandler.onException(e);
                        }
                        break;

                    case CommunicationConstants.JOINED:
                        StateManager.INSTANCE.setState(StateManager.ClientState.IN_GAME);
                        int size = scanner.nextInt();
                        boardClient.setBoardSize(size);
                        break;

                    case CommunicationConstants.COLOR_SET:
                        color = Color.valueOf(scanner.next());
                        generator.colorSet(color);
                        break;

                    case CommunicationConstants.YOUR_TURN:
                        generator.yourTurnBegan();
                        break;

                    case CommunicationConstants.MOVE_VALIDATED: {
                        boolean valid = scanner.next().equals(CommunicationConstants.OK);
                        generator.yourMoveValidated(valid);
                        break;
                    }

                    case CommunicationConstants.STONE_PLACED: {
                        Color color = Color.valueOf(scanner.next());
                        int x = scanner.nextInt();
                        int y = scanner.nextInt();
                        generator.stonePlaced(color, x, y);
                        break;
                    }

                    case CommunicationConstants.PASSED: {
                        Color color = Color.valueOf(scanner.next());
                        generator.passed(color);
                        break;
                    }

                    case CommunicationConstants.OPPONENT_DISCONNECTED:
                        ClientApplication.showError(new Exception("Przeciwnik się rozłączył"), false);
                        requestBoardList();
                        StateManager.INSTANCE.setState(StateManager.ClientState.ROOM_VIEW);
                        break;

                    case CommunicationConstants.OPPONENT_SURRENDERED:
                        ClientApplication.showInfo("Wygrałeś", "Przeciwnik się poddał");
                        requestBoardList();
                        StateManager.INSTANCE.setState(StateManager.ClientState.ROOM_VIEW);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionHandler.onException(e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                exceptionHandler.onException(e);
            }
        }
    }

    public void requestBoardList() {
        out.println(CommunicationConstants.GET_BOARDS + " " + GameServer.Filter.ALL.toString());
    }

    public void setGenerator(MoveGenerator generator) {
        this.generator = generator;
    }

    public void setRoomListUpdater(RoomListUpdater roomListUpdater) {
        this.roomListUpdater = roomListUpdater;
    }

    public void newBoard(String name, int size) {
        out.println(CommunicationConstants.NEW_BOARD + " " + name + " " + size);
    }

    public void playWithBot(int size) {
        out.println(CommunicationConstants.PLAY_WITH_BOT + " " + size);
    }

    @Override
    public void placeStone(Color color, int x, int y) {
        out.println(CommunicationConstants.STONE_PLACED + " " + Integer.toString(x) + " " + Integer.toString(y));
    }

    @Override
    public void pass(Color color) {
        out.println(CommunicationConstants.PASS);
    }

    @Override
    public void proposeScoring(Scoring scoring) {

    }

    @Override
    public void acceptScoring(Scoring scoring) {

    }

    @Override
    public void rejectScoring(Color color) {

    }

    @Override
    public void addMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void removeMoveGenerator(MoveGenerator generator) {

    }

    @Override
    public void surrender(Color color) {
        out.println(CommunicationConstants.SURRENDER);
    }

    public void setBoardClient(BoardClient boardClient) {
        this.boardClient = boardClient;
    }

    public void joinBoard(RoomListElement selectedItem) {
        out.println(CommunicationConstants.JOIN_BOARD + " " + selectedItem.getName());
    }
}
