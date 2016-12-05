package gogame.client;

import gogame.common.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetClient implements MovePerformer {
    private BufferedReader in;
    private PrintWriter out;

    NetClient(Socket socket) throws Exception{
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("HELLO");
        out.println("BYE");
    }

    @Override
    public void placeStone(Color color, int x, int y) {

    }

    @Override
    public void pass(Color color) {

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
