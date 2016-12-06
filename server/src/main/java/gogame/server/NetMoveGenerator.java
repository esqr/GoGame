package gogame.server;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetMoveGenerator extends Thread implements MoveGenerator {
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

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
                if (inputLine.equals("BYE")) {
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

    }

    @Override
    public void yourTurnBegan() {

    }

    @Override
    public void yourMoveValidated(boolean valid) {

    }

    @Override
    public void stonePlaced(Color color, int x, int y) {

    }

    @Override
    public void passed(Color color) {

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

    }
}
