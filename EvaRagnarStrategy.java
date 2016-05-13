package gomoku.strategies;

import gomoku.ComputerStrategy;
import gomoku.Location;
import gomoku.SimpleBoard;

/**
 * Created by Ragnar on 09.05.2016.
 * v0.01 - downloaded templated, named class w/ Eva, now need to bring in miniMax stuff and score stuff.
 */
public class EvaRagnarStrategy implements ComputerStrategy {
    private static final int WIN_COUNT = 5;
    private static final int MAX_DEPTH = 2;
    private static int[][] gameBoard;
    private static int rows;
    private static int columns;
    private int player;

    /***/
    @Override
    public Location getMove(SimpleBoard board, int player) {
        this.player = player;
        gameBoard = board.getBoard();
        rows = board.getHeight();
        columns = board.getWidth();
        ScoredMove scoredMove = minimax(player, 0, MAX_DEPTH, null);
        return scoredMove.location;
    }

    /***/
    @Override
    public String getName() {
        return "Eva ja Ragnar";
    }

    /***/
    public int getScore() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int player = gameBoard[row][col];
                if (player != SimpleBoard.EMPTY) {

                }
            }
        }
        return 0;
    }

    /***/
    public ScoredMove minimax(int player, int depth, int maxdepth, Location move) {
        if (depth == maxdepth) {
            int score = getScore();
            return new ScoredMove(move, score);
        }
        ScoredMove bestMove = new ScoredMove(-1000);
        if (player == this.player * (-1)) {
            bestMove.score = 1000;
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
                            bestMove = newScoredmove;
                        }
                    } else {
                        if (newScoredmove.score < bestMove.score) {
                            bestMove = newScoredmove;
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    /***/
    public boolean inARow5(int x, int y, int dx, int dy) {
        int player = gameBoard[x][y];
        for (int i = 0; i < WIN_COUNT; i++) {
            if (gameBoard[x + i * dx][y + i * dy] != player) {
                return false;
            }
        }
        return true;
    }

    public class ScoredMove {
        public Location location;
        public int score;

        public ScoredMove(Location location, int score) {
            this.location = location;
            this.score = score;
        }

        public ScoredMove(int score) {
            this.score = score;
        }
    }
}