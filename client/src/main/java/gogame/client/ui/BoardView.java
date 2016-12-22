package gogame.client.ui;

import gogame.common.collections.ObservableBoard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class BoardView extends Canvas {
    private static final Color lineColor = Color.GRAY;
    private static final Color whitesColor = Color.WHITESMOKE;
    private static final Color blacksColor = Color.rgb(0x22, 0x22, 0x22);
    private static final double padding = 5.0;

    private ObservableBoard board;
    private Pair<Integer, Integer> pointerPosition = null;

    private double marginV;
    private double marginH;
    private double fieldSize;
    private double stoneRadius;

    public BoardView() {
        super();
        widthProperty().addListener(event -> draw());
        heightProperty().addListener(event -> draw());

        onMouseExitedProperty().addListener(event -> {
            pointerPosition = null;
            draw();
        });

        setOnMouseMoved(event -> redrawIfNecessary(event));
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    public final int getBoardSize() {
        return board == null ? 0 : board.getSize();
    }

    private void draw() {
        double shorterSize = Math.min(getWidth(), getHeight());
        marginV = (getHeight() - shorterSize) + padding;
        marginH = (getWidth() - shorterSize) + padding;

        // calculate elements sizes
        double boardSideSize = shorterSize - padding;
        fieldSize = boardSideSize / (getBoardSize() - 1);
        stoneRadius = fieldSize * 0.45;

        // fix sizes
        boardSideSize = shorterSize - stoneRadius * 2 - padding;
        fieldSize = boardSideSize / (getBoardSize() - 1);

        marginV = marginV / 2 + stoneRadius;
        marginH = marginH / 2 + stoneRadius;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(1.5);
        gc.setStroke(lineColor);

        gc.beginPath();

        // draw board lines
        for (int i = 0; i < getBoardSize(); i++) {
            gc.moveTo(marginH + i * fieldSize, marginV);
            gc.lineTo(marginH + i * fieldSize, boardSideSize + marginV);

            gc.moveTo(marginH, marginV + i * fieldSize);
            gc.lineTo(boardSideSize + marginH, marginV + i * fieldSize);
        }
        gc.closePath();

        gc.stroke();

        if (board != null) {
            for (int i = 0; i < getBoardSize(); i++) {
                for (int j = 0; j < getBoardSize(); j++) {
                    gogame.common.Color color = board.getStone(i, j);
                    if (color != gogame.common.Color.NONE) {
                        gc.setFill(color == gogame.common.Color.BLACK ? blacksColor : whitesColor);
                        gc.strokeOval(marginH + i * fieldSize - stoneRadius,
                                marginV + j * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
                        gc.fillOval(marginH + i * fieldSize - stoneRadius,
                                marginV + j * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
                    }
                }
            }
        }

        if (pointerPosition != null) {
            gc.strokeOval(marginH + pointerPosition.getKey() * fieldSize - stoneRadius,
                    marginV + pointerPosition.getValue() * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
        }
    }

    private void redrawIfNecessary(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        Pair<Integer, Integer> newPointerPosition = calcPointerPosition(x, y);

        if ((newPointerPosition != null && !newPointerPosition.equals(pointerPosition)) ||
                (pointerPosition != null && !pointerPosition.equals(newPointerPosition))) {
            pointerPosition = newPointerPosition;
            draw();
        }

    }

    public Pair<Integer, Integer> calcPointerPosition(double x, double y) {
        for (int i = 0; i < getBoardSize(); i++) {
            double columnPosition = marginH + i * fieldSize;
            for (int j = 0; j < getBoardSize(); j++) {
                double rowPosition = marginV + j * fieldSize;
                if (Math.pow(x - columnPosition, 2) + Math.pow(y - rowPosition, 2) < Math.pow(stoneRadius, 2)) {
                    return new Pair<>(i, j);
                }
            }
        }

        return null;
    }

    public void setBoard(ObservableBoard board) {
        this.board = board;
        this.board.addObserver((o, arg) -> {
            draw();
        });
    }
}
