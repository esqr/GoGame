package gogame.server;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class NetMoveGenerator implements MoveGenerator {
    private PrintWriter writer;
    private BufferedReader reader;
    private Board performer;
    private Color color;
    private volatile boolean running = true;

    public NetMoveGenerator(PrintWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public void start() {
        SimpleLogger.log("game started; color: ", color);
        try {
            String inputLine;

            while (running && (inputLine = reader.readLine()) != null) {
                SimpleLogger.log("recv command: ", inputLine);
                Scanner scanner = new Scanner(inputLine);
                String command = scanner.next();

                if (command.equals(CommunicationConstants.STONE_PLACED)) {
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();

                    performer.placeStone(color, x, y);
                } else if (command.equals(CommunicationConstants.PASS)) {
                    performer.pass(color);
                } else if (command.equals(CommunicationConstants.BYE)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        performer.clientDisconnected(color);
        SimpleLogger.log("game ended; color: ", color);
    }

    @Override
    public void colorSet(Color color) {
        this.color = color;
        writer.println(CommunicationConstants.COLOR_SET + " " + color.toString());
    }

    @Override
    public void yourTurnBegan() {
        writer.println(CommunicationConstants.YOUR_TURN);
    }

    @Override
    public void yourMoveValidated(boolean valid) {
        writer.println(CommunicationConstants.MOVE_VALIDATED + " " + (valid ? CommunicationConstants.OK : CommunicationConstants.WRONG));
    }

    @Override
    public void stonePlaced(Color color, int x, int y) {
        writer.println(CommunicationConstants.STONE_PLACED + " " + color + " " + Integer.toString(x) + " " + Integer.toString(y));
    }

    @Override
    public void passed(Color color) {
        writer.println(CommunicationConstants.PASSED + " " + color.toString());
    }

    @Override
    public void scoringProposed(Scoring scoring) {

    }

    @Override
    public void scoringAccepted(Scoring scoring) {

    }

    @Override
    public void scoringRejected() {

    }

    @Override
    public void setMovePerformer(MovePerformer performer) {
        this.performer = (Board) performer;
    }

    @Override
    public void opponentDisconnected() {
        writer.println(CommunicationConstants.OPPONENT_DISCONNECTED);
        running = false;
    }

    public Board getBoard() {
        return performer;
    }
}
