package gogame.server;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetMoveGenerator extends Thread implements MoveGenerator {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private MovePerformer performer;

    public NetMoveGenerator(Socket socket) {
        this.socket = socket;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.println("HELLO");
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                Scanner scanner = new Scanner(inputLine);
                String command = scanner.next();

                if (command.equals("STONE")) {
                    Color color = Color.valueOf(scanner.next());
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();

                    performer.placeStone(color, x, y);
                } else if (command.equals("PASS")) {
                    Color color = Color.valueOf(scanner.next());

                    performer.pass(color);
                } else if (command.equals("BYE")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void colorSet(Color color) {
        writer.println("COLOR " + color.toString());
    }

    @Override
    public void yourTurnBegan() {
        writer.println("YOUR_TURN");
    }

    @Override
    public void yourMoveValidated(boolean valid) {
        writer.println("VALIDATED " + (valid ? "OK" : "WRONG"));
    }

    @Override
    public void stonePlaced(Color color, int x, int y) {
        writer.println("STONE " + Integer.toString(x) + " " + Integer.toString(y));
    }

    @Override
    public void passed(Color color) {
        writer.println("PASSED " + color.toString());
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
        this.performer = performer;
    }
}
