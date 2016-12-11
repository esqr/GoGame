package gogame.client.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BoardCanvas extends Canvas {
    private static final int DEFAULT_BOARD_SIZE = 19;
    private static final Color lineColor = Color.GRAY;
    private static final Color whitesColor = Color.WHITESMOKE;
    private static final Color blacksColor = Color.BLACK;

    public BoardCanvas() {
        super();
        widthProperty().addListener(event -> draw());
        heightProperty().addListener(event -> draw());
    }

    public BoardCanvas(double width, double height) {
        super(width, height);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    private IntegerProperty boardSize;

    public final void setBoardSize(int value) {
        boardSizeProperty().set(value);
    }

    public final int getBoardSize() {
        return boardSize == null ? DEFAULT_BOARD_SIZE : boardSize.get();
    }

    public final IntegerProperty boardSizeProperty() {
        if (boardSize == null) {
            boardSize = new IntegerPropertyBase() {
                @Override
                public Object getBean() {
                    return BoardCanvas.this;
                }

                @Override
                public String getName() {
                    return "boardSize";
                }
            };
        }
        return boardSize;
    }

    private void draw() {
        double shorterSize = Math.min(getWidth(), getHeight());
        double marginV = (getHeight() - shorterSize);
        double marginH = (getWidth() - shorterSize);

        // calculate elements sizes
        double boardSideSize = shorterSize;
        double fieldSize = boardSideSize / (getBoardSize() - 1);
        double stoneRadius = fieldSize / 3;

        // fix sizes
        boardSideSize = shorterSize - stoneRadius * 2;
        fieldSize = boardSideSize / (getBoardSize() - 1);

        marginV = marginV / 2 + stoneRadius;
        marginH = marginH / 2 + stoneRadius;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(1.0);
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
    }
}
