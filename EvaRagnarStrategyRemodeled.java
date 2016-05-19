package gomoku.strategies;

import gomoku.ComputerStrategy;
import gomoku.Location;
import gomoku.SimpleBoard;

/**
 * Created by Ragnar on 09.05.2016.
 * v0.01 - downloaded templated, named class w/ Eva, now need to bring in miniMax stuff and score stuff.
 * v0.02 - implemented minimax to return move also, not only score. now need to bring in searching for the scores.
 * v0.03 - can detect 5 in a row and 4 open row, soon will add others.
 * v0.04 - added closed 4 rows, need to change counting system and therefore score too, will turn much faster after this.
 * v0.05 - changed scoring mechanics, can be upgraded even more, but leave it for now. need to include diagonal situations
 * and turn it to alpha-beta to make it faster.
 */
public class EvaRagnarStrategyRemodeled implements ComputerStrategy {
    /***/
    private static final int COUNT_5 = 5;
    /***/
    private static final int COUNT_4 = 4;
    /***/
    private static final int COUNT_3 = 3;
    /***/
    private static final int COUNT_2 = 2;
    /***/
    private static final int COUNT_1 = 1;
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
        System.out.println(bestMove.location.getRow() + " " + bestMove.location.getColumn());
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
                    switch (counter(row, col, 1, 0)) { //check vertical lineups
                        case 5:
                            System.out.println("5 ROW VERTICAL");
                            score += SCORE_5_ROW * playertype;
                            break;
/*                        case 4:
                            if (row > 0 && row < rows - COUNT_4) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY && gameBoard[row + COUNT_4][col] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW VERTICAL OPEN");
                                    score += SCORE_4_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col] != 0 ^ gameBoard[row + COUNT_4][col] != 0) {
                                    System.out.println("4 ROW VERTICAL CLOSED");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            } else if (row == 0) {
                                if (gameBoard[row + COUNT_4][col] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            } else if (row == rows - COUNT_4) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 3:
                            if (row > 0 && row < rows - COUNT_3) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY && gameBoard[row + COUNT_3][col] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW VERTICAL OPEN");
                                    score += SCORE_3_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col] != 0 ^ gameBoard[row + COUNT_3][col] != 0) {
                                    System.out.println("3 ROW VERTICAL CLOSED");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            } else if (row == 0) {
                                if (gameBoard[row + COUNT_3][col] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            } else if (row == rows - COUNT_3) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 2:
                            if (row > 0 && row < rows - COUNT_2) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY && gameBoard[row + COUNT_2][col] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW VERTICAL OPEN");
                                    score += SCORE_2_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col] != 0 ^ gameBoard[row + COUNT_2][col] != 0) {
                                    System.out.println("2 ROW VERTICAL CLOSED");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            } else if (row == 0) {
                                if (gameBoard[row + COUNT_2][col] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            } else if (row == rows - COUNT_2) {
                                if (gameBoard[row - 1][col] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW VERTICAL CLOSED EDGE");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            }
                            break;*/
                        case 1:
                            break;
                    }
                    switch (counter(row, col, 0, 1)) { //check horizontal lineups
                        case 5:
                            System.out.println("5 ROW HORIZONTAL");
                            score += SCORE_5_ROW * playertype;
                            break;
/*                        case 4:
                            if (col > 0 && col < columns - COUNT_4) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY && gameBoard[row][col + COUNT_4] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW HORIZONTAL OPEN");
                                    score += SCORE_4_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row][col - 1] != 0 ^ gameBoard[row][col + COUNT_4] != 0) {
                                    System.out.println("4 ROW HORIZONTAL CLOSED");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            } else if (col == 0) {
                                if (gameBoard[row][col + COUNT_4] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            } else if (col == columns - COUNT_4) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY) {
                                    System.out.println("4 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 3:
                            if (col > 0 && col < columns - COUNT_3) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY && gameBoard[row][col + COUNT_3] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW HORIZONTAL OPEN");
                                    score += SCORE_3_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row][col - 1] != 0 ^ gameBoard[row][col + COUNT_3] != 0) {
                                    System.out.println("3 ROW HORIZONTAL CLOSED");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            } else if (col == 0) {
                                if (gameBoard[row][col + COUNT_3] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            } else if (col == columns - COUNT_3) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY) {
                                    System.out.println("3 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 2:
                            if (col > 0 && col < columns - COUNT_2) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY && gameBoard[row][col + COUNT_2] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW HORIZONTAL OPEN");
                                    score += SCORE_2_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row][col - 1] != 0 ^ gameBoard[row][col + COUNT_2] != 0) {
                                    System.out.println("2 ROW HORIZONTAL CLOSED");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            } else if (col == 0) {
                                if (gameBoard[row][col + COUNT_2] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            } else if (col == columns - COUNT_2) {
                                if (gameBoard[row][col - 1] == SimpleBoard.EMPTY) {
                                    System.out.println("2 ROW HORIZONTAL CLOSED EDGE");
                                    score += SCORE_2_ROW_CLOSED * playertype;
                                }
                            }
                            break;*/
                        case 1:
                            break;
                    }
                    switch (counter(row, col, 1, -1)) { //check vertical left lineups
                        case 5:
                            System.out.println("5 ROW VERTICAL LEFT");
                            score += SCORE_5_ROW * playertype;
                            break;
                        case 4:
                            if (row > 0 && row <= rows - COUNT_5 && col < columns - 1 && col >= COUNT_4) {
                                if (gameBoard[row - 1][col + 1] == 0 && gameBoard[row + COUNT_4][col - COUNT_4] == 0) {
                                    System.out.println("4 ROW VERTICAL LEFT OPEN");
                                    score += SCORE_4_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col + 1] != 0 ^ gameBoard[row + COUNT_4][col - COUNT_4] != 0) {
                                    System.out.println("4 ROW VERTICAL LEFT CLOSED");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 3:
                            /*if (row > 0 && row <= rows - COUNT_4 && col < columns - 1 && columns >= COUNT_3) {
                                if (gameBoard[row - 1][col + 1] == 0 && gameBoard[row + COUNT_3][col - COUNT_3] == 0) {
                                    System.out.println("3 ROW VERTICAL LEFT OPEN");
                                    score += SCORE_3_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col + 1] != 0 ^ gameBoard[row + COUNT_3][col - COUNT_3] != 0) {
                                    System.out.println("3 ROW VERTICAL LEFT CLOSED");
                                    score += SCORE_3_ROW_CLOSED * playertype;
                                }
                            }*/
                            break;
                        case 2:
                            break;
                        case 1:
                            break;
                    }
                    switch (counter(row, col, 1, 1)) { //check vertical right lineups
                        case 5:
                            System.out.println("5 ROW VERTICAL RIGHT");
                            score += SCORE_5_ROW * playertype;
                            break;
                        case 4:
                            System.out.println("jo" + row + "jo" + col);
                            if (row > 0 && row <= rows - COUNT_5 && col <= columns - COUNT_5 && col > 0) {
                                System.out.println("jojo" + row + "jojo" + col);
                                if (gameBoard[row - 1][col - 1] == 0 && gameBoard[row + COUNT_4][col + COUNT_4] == 0) {
                                    System.out.println("4 ROW VERTICAL RIGHT OPEN");
                                    score += SCORE_4_ROW_OPEN * playertype;
                                }
                                if (gameBoard[row - 1][col - 1] != 0 ^ gameBoard[row + COUNT_4][col + COUNT_4] != 0) {
                                    System.out.println("4 ROW VERTICAL RIGHT CLOSED");
                                    score += SCORE_4_ROW_CLOSED * playertype;
                                }
                            }
                            break;
                        case 3:
                            break;
                        case 2:
                            break;
                        case 1:
                            break;
                    }
                }
            }
        }
        return score;
    }

    /***/
    public int counter(int x, int y, int dx, int dy) {
        int count = 0;
        int player = gameBoard[x][y];
        for (int i = 0; i < COUNT_5; i++) {
            if ((x + i * dx > -1) && (y + i * dy > -1) && (x + i * dx < rows) && (y + i * dy < columns)) {
                if (gameBoard[x + i * dx][y + i * dy] == player) {
                    count++;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return count;
    }


    /***/
    public boolean gameOver() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int player = gameBoard[row][col];
                if (player != SimpleBoard.EMPTY) {
                    if (counter(row, col, 1, 0) == COUNT_5) { // lets check vertical lineup
                        return true;
                    }
                    if (counter(row, col, 1, -1) == COUNT_5) { // lets check vertical left diagonal
                        System.out.println("2");
                        return true;
                    }
                    if (counter(row, col, 0, 1) == COUNT_5) { // lets check horizonal lineup
                        System.out.println("3");
                        return true;
                    }
                    if (counter(row, col, 1, 1) == COUNT_5) { // lets check vertical right diagonal
                        System.out.println("4");
                        return true;
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