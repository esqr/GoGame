package gogame.client.ui;

import gogame.client.misc.Stone;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class BoardCanvas extends Canvas {
    private static final int DEFAULT_BOARD_SIZE = 19;
    private static final Color lineColor = Color.GRAY;
    private static final Color whitesColor = Color.WHITESMOKE;
    private static final Color blacksColor = Color.rgb(0x22, 0x22, 0x22);
    private static final double padding = 5.0;

    private ObservableList<Stone> stones;
    private Pair<Integer, Integer> blankStonePosition = null;

    private double marginV;
    private double marginH;
    private double fieldSize;
    private double stoneRadius;
    private IntegerProperty boardSize;

    public BoardCanvas() {
        super();
        widthProperty().addListener(event -> draw());
        heightProperty().addListener(event -> draw());

        onMouseExitedProperty().addListener(event -> {
            blankStonePosition = null;
            draw();
        });

        setOnMouseMoved(event -> redrawIfNecessary(event));
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    public final int getBoardSize() {
        return boardSize == null ? DEFAULT_BOARD_SIZE : boardSize.get();
    }

    public final void setBoardSize(int value) {
        boardSizeProperty().set(value);
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

        if (stones != null) {
            for (Stone stone : stones) {
                gc.setFill(stone.getColor() == gogame.common.Color.BLACK ? blacksColor : whitesColor);
                gc.strokeOval(marginH + stone.getPosX() * fieldSize - stoneRadius,
                        marginV + stone.getPosY() * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
                gc.fillOval(marginH + stone.getPosX() * fieldSize - stoneRadius,
                        marginV + stone.getPosY() * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
            }
        }

        if (blankStonePosition != null) {
            gc.strokeOval(marginH + blankStonePosition.getKey() * fieldSize - stoneRadius,
                    marginV + blankStonePosition.getValue() * fieldSize - stoneRadius, stoneRadius * 2, stoneRadius * 2);
        }

        System.out.println("woo");
    }

    private void redrawIfNecessary(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        Pair<Integer, Integer> newBlankStonePosition = calcBlankStonePosition(x, y);

        if ((newBlankStonePosition != null && !newBlankStonePosition.equals(blankStonePosition)) ||
                (blankStonePosition != null && !blankStonePosition.equals(newBlankStonePosition))) {
            blankStonePosition = newBlankStonePosition;
            draw();
        }

    }

    private Pair<Integer, Integer> calcBlankStonePosition(double x, double y) {
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

    public void setStoneList(ObservableList<Stone> stones) {
        this.stones = stones;
        stones.addListener(new ListChangeListener<Stone>() {
            @Override
            public void onChanged(Change<? extends Stone> c) {
                draw();
            }
        });
    }
}
