package gogame.server;

import gogame.common.Board;
import gogame.common.Color;
import gogame.common.CommunicationConstants;
import gogame.common.Stone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class NetMoveGeneratorTest {
    private PipedOutputStream pos = null;
    private PipedInputStream is = null;
    private ByteArrayOutputStream os = null;
    private NetMoveGenerator netMoveGenerator = null;
    private Board movePerformer = null;


    @Before
    public void setUp() throws Exception {
        pos = new PipedOutputStream();
        is = new PipedInputStream(pos);
        os = new ByteArrayOutputStream();
        movePerformer = mock(Board.class);
        netMoveGenerator = new NetMoveGenerator(new PrintWriter(os, true),
                new BufferedReader(new InputStreamReader(is)));
        netMoveGenerator.setMovePerformer(movePerformer);
    }

    @Test(timeout = 5000)
    public void byeCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).clientDisconnected(null);
    }

    @Test(timeout = 5000)
    public void stonePlacedCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.STONE_PLACED + " 1 2\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).placeStone(null, 1, 2);
    }

    @Test(timeout = 5000)
    public void passCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.PASS + "\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();

        verify(movePerformer, times(1)).pass(null);
    }

    @Test(timeout = 5000)
    public void surrenderCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.SURRENDER + "\n").getBytes());
        thread.join();

        verify(movePerformer, times(1)).surrender(null);
    }

    @Test(timeout = 5000)
    public void scoringAcceptCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.ACCEPT + "\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).acceptScoring(null);
    }

    @Test(timeout = 5000)
    public void scoringRejectCommand() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.REJECT + "\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).rejectScoring(null);
    }

    @Test(timeout = 5000)
    public void scoringAliveCommand() throws Exception {
        List<Stone> expectedStones = Arrays.asList(new Stone(1, 2, null), new Stone(3, 4, null));

        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.ALIVE + " 1 2 3 4\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).proposeAlive(argThat(new ArgumentMatcher<List<Stone>>() {
            @Override
            public boolean matches(List<Stone> stones) {
                for (int i = 0; i < stones.size(); i++) {
                    if (stones.get(i).getColor() != expectedStones.get(i).getColor()) return false;
                    if (stones.get(i).getPosY() != expectedStones.get(i).getPosY()) return false;
                    if (stones.get(i).getPosX()!= expectedStones.get(i).getPosX()) return false;
                }

                return true;
            }
        }));
    }

    @Test(timeout = 5000)
    public void scoringDeadCommand() throws Exception {
        List<Stone> expectedStones = Arrays.asList(new Stone(1, 2, null), new Stone(3, 4, null));

        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        pos.write((CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.DEAD + " 1 2 3 4\n").getBytes());
        pos.write((CommunicationConstants.BYE + "\n").getBytes());
        thread.join();
        verify(movePerformer, times(1)).proposeDead(argThat(new ArgumentMatcher<List<Stone>>() {
            @Override
            public boolean matches(List<Stone> stones) {
                for (int i = 0; i < stones.size(); i++) {
                    if (stones.get(i).getColor() != expectedStones.get(i).getColor()) return false;
                    if (stones.get(i).getPosY() != expectedStones.get(i).getPosY()) return false;
                    if (stones.get(i).getPosX()!= expectedStones.get(i).getPosX()) return false;
                }

                return true;
            }
        }));
    }

    @Test(timeout = 5000)
    public void testIOException() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();
        is.close();
        thread.join();

        verify(movePerformer, times(1)).clientDisconnected(null);
    }



    // interface implementation tests

    @Test
    public void colorSet() throws Exception {
        netMoveGenerator.colorSet(Color.BLACK);
        assertEquals(CommunicationConstants.COLOR_SET + " " + Color.BLACK.toString() + "\n", os.toString());
    }

    @Test
    public void yourTurnBegan() throws Exception {
        netMoveGenerator.yourTurnBegan();
        assertEquals(CommunicationConstants.YOUR_TURN + "\n", os.toString());
    }

    @Test
    public void yourMoveValidated() throws Exception {
        netMoveGenerator.yourMoveValidated(false);
        netMoveGenerator.yourMoveValidated(true);

        assertEquals(CommunicationConstants.MOVE_VALIDATED + " " + CommunicationConstants.WRONG + "\n" +
                        CommunicationConstants.MOVE_VALIDATED + " " + CommunicationConstants.OK + "\n",
                os.toString());
    }

    @Test
    public void stonePlaced() throws Exception {
        netMoveGenerator.stonePlaced(Color.BLACK, 1, 2);
        assertEquals(CommunicationConstants.STONE_PLACED + " " + Color.BLACK + " " + Integer.toString(1) + " " + Integer.toString(2) + "\n",
                os.toString());
    }

    @Test
    public void passed() throws Exception {
        netMoveGenerator.passed(Color.BLACK);
        assertEquals(CommunicationConstants.PASSED + " " + Color.BLACK + "\n",
                os.toString());
    }

    @Test
    public void scoringAccepted() throws Exception {
        netMoveGenerator.scoringAccepted();
        // todo
    }

    @Test
    public void scoringRejected() throws Exception {
        netMoveGenerator.scoringRejected();
        assertEquals(CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.REJECTED + "\n",
                os.toString());
    }

    @Test
    public void opponentDisconnected() throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                netMoveGenerator.start();
            }
        };
        thread.start();

        netMoveGenerator.opponentDisconnected();
        thread.join();
        assertEquals(CommunicationConstants.OPPONENT_DISCONNECTED + "\n",
                os.toString());
    }

    @Test
    public void opponentSurrendered() throws Exception {
        netMoveGenerator.opponentSurrendered();
        assertEquals(CommunicationConstants.OPPONENT_SURRENDERED + "\n",
                os.toString());
    }

    @Test
    public void scoringStarted() throws Exception {
        netMoveGenerator.scoringStarted();
        assertEquals(CommunicationConstants.SCORING + " " + CommunicationConstants.Scoring.STARTED + "\n",
                os.toString());
    }

    @Test
    public void aliveProposed() throws Exception {
        List<Stone> stones = Arrays.asList(new Stone(1, 2, null), new Stone(3, 4, null));

        StringBuilder sb = new StringBuilder();
        sb.append(CommunicationConstants.SCORING).append(" ")
                .append(CommunicationConstants.Scoring.ALIVE);

        for (Stone stone : stones) {
            sb.append(" ").append(stone.getPosX()).append(" ").append(stone.getPosY());
        }

        sb.append("\n");

        netMoveGenerator.aliveProposed(stones);

        assertEquals(sb.toString(), os.toString());

    }

    @Test
    public void deadProposed() throws Exception {
        List<Stone> stones = Arrays.asList(new Stone(1, 2, null), new Stone(3, 4, null));

        StringBuilder sb = new StringBuilder();
        sb.append(CommunicationConstants.SCORING).append(" ")
                .append(CommunicationConstants.Scoring.DEAD);

        for (Stone stone : stones) {
            sb.append(" ").append(stone.getPosX()).append(" ").append(stone.getPosY());
        }

        sb.append("\n");

        netMoveGenerator.deadProposed(stones);

        assertEquals(sb.toString(), os.toString());

    }

    @Test
    public void getBoard() throws Exception {
        assertSame(movePerformer, netMoveGenerator.getBoard());
    }
}