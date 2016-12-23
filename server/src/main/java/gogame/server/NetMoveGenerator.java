package gogame.server;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
                } else if (command.equals(CommunicationConstants.SURRENDER)) {
                    running = false;
                    performer.surrender(color);
                    break;
                } else if (command.equals(CommunicationConstants.SCORING)) {
                    String proposed = scanner.next();

                    if (proposed.equals(CommunicationConstants.Scoring.ACCEPT)) {
                        performer.acceptScoring(color);
                        continue;
                    } else if (proposed.equals(CommunicationConstants.Scoring.REJECT)) {
                        performer.rejectScoring(color);
                        continue;
                    }

                    List<Stone> proposedScoring = new ArrayList<>();
                    try {
                        while (true) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();

                            proposedScoring.add(new Stone(x, y, null));
                        }
                    } catch (NoSuchElementException ignored) {}

                    if (proposed.equals(CommunicationConstants.Scoring.ALIVE)) {
                        performer.proposeAlive(proposedScoring);
                    } else if (proposed.equals(CommunicationConstants.Scoring.DEAD)) {
                        performer.proposeDead(proposedScoring);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (running) {
            performer.clientDisconnected(color);
        }

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
    public void scoringAccepted() {
        // todo
    }

    @Override
    public void scoringRejected() {
        writer.println(CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.REJECTED);
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

    @Override
    public void opponentSurrendered() {
        writer.println(CommunicationConstants.OPPONENT_SURRENDERED);
        running = false;
    }

    @Override
    public void scoringStarted() {
        writer.println(CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.STARTED);
    }

    @Override
    public void aliveProposed(List<Stone> alive) {
        StringBuilder sb = new StringBuilder();
        sb.append(CommunicationConstants.SCORING).append(" ")
                .append(CommunicationConstants.Scoring.ALIVE);

        for (Stone stone : alive) {
            sb.append(" ").append(stone.getPosX()).append(" ").append(stone.getPosY());
        }

        writer.println(sb.toString());
    }

    @Override
    public void deadProposed(List<Stone> dead) {
        StringBuilder sb = new StringBuilder();
        sb.append(CommunicationConstants.SCORING).append(" ")
                .append(CommunicationConstants.Scoring.DEAD);

        for (Stone stone : dead) {
            sb.append(" ").append(stone.getPosX()).append(" ").append(stone.getPosY());
        }

        writer.println(sb.toString());
    }

    public Board getBoard() {
        return performer;
    }
}
