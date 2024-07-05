package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * This class is used to implement the player object
 * It includes two players*/
public class Player {
    public ReturnType p1Turn(IO io, int[][] numberArray, char inputChar, boolean vertical) {
        Move p1Move = new Move();
        int inputInt = inputChar - '0';
        return p1Move.moveOfP1ReturnFinalPosition(io, numberArray, inputInt, vertical);
    }

    public ReturnType p2Turn(IO io, int[][] numberArray, char inputChar, boolean vertical, boolean bmf) {
        Move p2Move = new Move();
        int inputInt = inputChar - '0';
        return p2Move.moveOfP2ReturnFinalPosition(io, numberArray, inputInt, vertical, bmf);
    }
}
