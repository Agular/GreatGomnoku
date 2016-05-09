package gomoku.strategies;

import gomoku.ComputerStrategy;
import gomoku.Location;
import gomoku.SimpleBoard;

/**
 * Created by Ragnar on 09.05.2016.
 * v0.01 - downloaded templated, named class w/ Eva, now need to bring in miniMax stuff and score stuff.
 */
public class DickbuttStrategy implements ComputerStrategy {
    @Override
    public Location getMove(SimpleBoard board, int player) {
        // let's operate on 2-d array
        int[][] b = board.getBoard();
        for (int row = b.length - 1; row >= 0; row--) {
            for (int col = b[0].length - 1; col >= 0; col--) {
                if (b[row][col] == SimpleBoard.EMPTY) {
                    // first empty location
                    return new Location(row, col);
                }
            }
        }
        return null;
    }
    /***/
    @Override
    public String getName() {
        return "Dickbutt";
    }
}
