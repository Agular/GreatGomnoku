package gomoku.strategies;

import gomoku.ComputerStrategy;
import gomoku.Location;
import gomoku.SimpleBoard;

/**
 * Created by Ragnar on 09.05.2016.
 * v0.01 - downloaded templated, named class w/ Eva, now need to bring in miniMax stuff and score stuff.
 * v0.02 - implemented minimax to return move also, not only score. now need to bring in searching for the scores.
 * v0.03 - can detect 5 in a row and 4 open row, soon will add others.
 */
public class EvaRagnarStrategy implements ComputerStrategy {
    /***/
    private static final int WIN_COUNT = 5;
    /***/
    private static final int MAX_DEPTH = 3;
    /***/
    private static int[][] gameBoard;
    /***/
    private static int rows;
    /***/
    private static int columns;
    /***/
    private int player;
    /***/
    private static final int MAX_SCORE = 1000;
    /***/
    private static final int MIN_SCORE = -1000;
    /***/
    private static final int SCORE_5_ROW = 999;
    /***/
    private static final int SCORE_4_ROW_OPEN = 777;
    /***/
    private static final int SCORE_4_ROW_CLOSED = 555;
    /***/
    private static final int SCORE_3_ROW_OPEN = 444;
    /***/
    private static final int SCORE_3_ROW_CLOSED = 333;
    /***/
    private static final int SCORE_2_ROW_OPEN = 222;
    /***/
    private static final int SCORE_2_ROW_CLOSED = 111;

    /***/
    @Override
    public Location getMove(SimpleBoard board, int player) {
        this.player = player;
        gameBoard = board.getBoard();
        rows = board.getHeight();
        columns = board.getWidth();
        ScoredMove scoredMove = minimax(player, 1, MAX_DEPTH, null);
        System.out.println(scoredMove.score);
        return scoredMove.location;
    }

    /***/
    @Override
    public String getName() {
        return "Eva ja Ragnar";
    }


    /***/
    public ScoredMove minimax(int player, int depth, int maxdepth, Location move) {
        if (depth == maxdepth || gameOver()) {
            int score = getScore();
            return new ScoredMove(move, score);
        }
        ScoredMove bestMove = new ScoredMove(MIN_SCORE);
        if (player == this.player * (-1)) {
            bestMove.score = MAX_SCORE;
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (gameBoard[row][col] == 0) {
                    gameBoard[row][col] = player;
                    Location newMove = new Location(row, col);
                    ScoredMove newScoredmove = minimax(player * (-1), depth + 1, maxdepth, newMove);
                    gameBoard[row][col] = 0;
                    if (player == this.player) {
                        if (newScoredmove.score > bestMove.score) {
                            System.out.println();
                            System.out.println("+ row: " + row + "col: " + col);
                            bestMove.score = newScoredmove.score;
                            bestMove.location = newMove;
                        }
                    } else {
                        if (newScoredmove.score < bestMove.score) {
                            System.out.println("- row: " + row + "col: " + col);
                            bestMove.score = newScoredmove.score;
                            bestMove.location = newMove;
                        }
                    }
                }
            }
        }
        System.out.println(bestMove.location.getRow()+" "+bestMove.location.getColumn());
        return bestMove;
    }


    /***/
    public int getScore() {
        int score = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int player = gameBoard[row][col];
                int playertype = 1;
                if (this.player != player) {
                    playertype = -1;
                }
                if (player != SimpleBoard.EMPTY) {
                    if (row <= rows - WIN_COUNT) {// lets go down
                        if (inARow5(row, col, 1, 0)) { // lets check vertical lineup 5
                            System.out.println("verticalwin");
                            score += SCORE_5_ROW * playertype;
                        }
                        if (row > 0 && inARow4Open(row, col, 1, 0)) { // check vertical open 4
                            System.out.println("OPEN 4 ROW VERTICAL ");
                            score += SCORE_4_ROW_OPEN * playertype;
                        }
                        if (col >= WIN_COUNT - 1 && inARow5(row, col, 1, -1)) { // lets check vertical left diagonal 5
                            System.out.println("verticalLEFT");
                            score += SCORE_5_ROW * playertype;
                        }
                        /*if (col > WIN_COUNT - 1 && row > 0 && inARow4Open(row, col, 1, -1)) { //vertical left open 4
                            System.out.println("OPEN 4 VERTICAL LEFT");
                            score += SCORE_4_ROW_OPEN * playertype;
                        }*/
                    }
                    if (col <= columns - WIN_COUNT) { // lets check right
                        if (inARow5(row, col, 0, 1)) { // lets check horizonal lineup 5
                            System.out.println("horizontalwin");
                            score += SCORE_5_ROW * playertype;
                        }
                        if (col > 0 && inARow4Open(row, col, 0, 1)) {
                            System.out.println("OPEN 4 HORIZONTAL");
                            score += SCORE_4_ROW_OPEN * playertype;
                        }
                        if (row <= rows - WIN_COUNT && inARow5(row, col, 1, 1)) { // lets check vertical right diagonal 5
                            System.out.println("verticalRIGHT");
                            score += SCORE_5_ROW * playertype;
                        }
                        /*if (row > 0 && col <= columns - WIN_COUNT && inARow4Open(row, col, 1, 1)){ // vertical right open 4
                            System.out.println("OPEN 4 VERTICAL RIGHT");
                            score += SCORE_4_ROW_OPEN * playertype;
                        }*/
                    }

                }
            }
        }
        return score;
    }

    /***/
    public boolean inARow5(int x, int y, int dx, int dy) {
        int player = gameBoard[x][y];
        for (int i = 0; i < WIN_COUNT; i++) {
            if (gameBoard[x + i * dx][y + i * dy] != player) {
                return false;
            }
        }
        System.out.println("victory row: " + x + " column: " + y);
        return true;
    }

    /***/
    public boolean inARow4Open(int x, int y, int dx, int dy) {
        int player = gameBoard[x][y];
        for (int i = 0; i < WIN_COUNT - 1; i++) {
            if (gameBoard[x + i * dx][y + i * dy] != player) {
                return false;
            }
        }
        if (gameBoard[x - dx][y - dy] == 0 && gameBoard[x + (WIN_COUNT - 1) * dx][y + (WIN_COUNT - 1) * dy] == 0) {
            System.out.println("4open");
            return true;
        } else {
            return false;
        }
    }

    /***/
    public boolean inARow4Closed(int x, int y, int dx, int dy) {
        int player = gameBoard[x][y];
        for (int i = 0; i < WIN_COUNT - 1; i++) {
            if (gameBoard[x + i * dx][y + i * dy] != player) {
                return false;
            }
        }
        if (gameBoard[x - dx][y - dy] == 0 ^ gameBoard[x + (WIN_COUNT - 1) * dx][y + (WIN_COUNT - 1) * dy] == 0) {
            System.out.println("4closed");
            return true;
        } else {
            return false;
        }
    }

    /***/
    public boolean gameOver() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int player = gameBoard[row][col];
                if (player != SimpleBoard.EMPTY) {
                    if (row <= rows - WIN_COUNT) {// lets go down
                        if (inARow5(row, col, 1, 0)) { // lets check vertical lineup
                            return true;
                        }
                        if (col >= WIN_COUNT - 1 && inARow5(row, col, 1, -1)) { // lets check vertical left diagonal
                            System.out.println("2");
                            return true;
                        }
                    }
                    if (col <= columns - WIN_COUNT) { // lets check right
                        if (inARow5(row, col, 0, 1)) { // lets check horizonal lineup
                            System.out.println("3");
                            return true;
                        }
                        if (row <= rows - WIN_COUNT && inARow5(row, col, 1, 1)) { // lets check vertical right diagonal
                            System.out.println("4");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /***/
    public class ScoredMove {
        /***/
        public Location location;
        /***/
        public int score;

        /***/
        public ScoredMove(Location location, int score) {
            this.location = location;
            this.score = score;
        }

        /***/
        public ScoredMove(int score) {
            this.score = score;
        }
    }
}