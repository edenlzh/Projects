package kalah;

import com.qualitascorpus.testsupport.IO;
import java.util.ArrayList;
/**
 * Memento Class for the Memento design pattern
 *
 * The GameBoard class is responsible for the board's state at
 * any time, with the displayBoard method which acts with a
 * toString method to display the board in a formatted way to
 * the user.
 */
public class GameBoard {
    private ArrayList<Pit> _playingBoardState;
    private boolean _isP1Turn;
    private final int P1STOREINDEX = 6;
    private final int P2STOREINDEX = 13;

    /**
     * Constructor
     * @param currentBoardState, PlayerPits that comprise the current state of the board
     * @param isP1turn
     */
    GameBoard(ArrayList<Pit> currentBoardState, boolean isP1turn) {
        _playingBoardState = new ArrayList<>(currentBoardState);
        _isP1Turn = isP1turn;
    }

    /**
     * Constructor
     * @param currentBoardState
     */
    GameBoard(ArrayList<Pit> currentBoardState) {
        _playingBoardState = new ArrayList<>(currentBoardState);
    }

    /**
     * @return TRUE if P1 turn, FALSE for p2
     */
    public boolean getIsP1Turn() { return _isP1Turn; }

    /**
     * @param i - index of the Pit
     * @return - the Pit at the stated index
     */
    public Pit getPit(int i) {
        return _playingBoardState.get(i);
    }

    /**
     * Formats Board display to the user
     * @param io
     */
    public void displayBoard(IO io) {
        String boardEdge = "+----+-------+-------+-------+-------+-------+-------+----+";
        io.println(boardEdge);
        io.println("| P2 | 6[" +toString(_playingBoardState.get(12))+ "] | 5[" +toString(_playingBoardState.get(11))+ "] | 4[" +toString(_playingBoardState.get(10))+ "] | 3[" +toString(_playingBoardState.get(9))+ "] | 2[" +toString(_playingBoardState.get(8))+ "] | 1[" +toString(_playingBoardState.get(7))+ "] | " +toString(_playingBoardState.get(P1STOREINDEX))+ " |");
        io.println("|    |-------+-------+-------+-------+-------+-------|    |");
        io.println("| " +toString(_playingBoardState.get(P2STOREINDEX))+ " | 1[" +toString(_playingBoardState.get(0))+ "] | 2[" +toString(_playingBoardState.get(1))+ "] | 3[" +toString(_playingBoardState.get(2))+ "] | 4[" +toString(_playingBoardState.get(3))+ "] | 5[" +toString(_playingBoardState.get(4))+ "] | 6[" +toString(_playingBoardState.get(5))+ "] | P1 |");
        io.print(boardEdge);
        io.println("");
    }

    public static String toString(Pit pit) {
        if (pit.getNumSeed() >= 0 && pit.getNumSeed() < 10) {
            return " " + pit.getNumSeed();
        }
        else {
            return Integer.toString(pit.getNumSeed());
        }
    }

    /**
     * @return the index of P1 Store in ArrayList
     */
    public int getP1STOREINDEX() {
        return P1STOREINDEX;
    }

    /**
     * @return the index of P2 Store in ArrayList
     */
    public int getP2STOREINDEX() {
        return P2STOREINDEX;
    }
}

 
