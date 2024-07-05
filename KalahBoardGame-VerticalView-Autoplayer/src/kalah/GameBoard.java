package kalah;

import com.qualitascorpus.testsupport.IO;

/** This class is responsible for the game's board,
 *  including printing the game board and override the toString method.
 *  To implement new features this class has a new game board that is vertical,
 *  and it uses two boolean variants to make judgments.
 */
public class GameBoard {
    public void printGameBoard(IO io, boolean vertical, int[][] numberArray) {
        if (!vertical) {
            String[][] kalahBoard = new String[5][];
            //fixed parts of the gameboard
            kalahBoard[0] = new String[]{"+----+-------+-------+-------+-------+-------+-------+----+"};
            kalahBoard[1] = new String[]{"| P2 | 6[" + toString(numberArray[1][5]) + "] | 5[" + toString(numberArray[1][4]) + "] | 4[" + toString(numberArray[1][3]) + "] | 3[" + toString(numberArray[1][2]) + "] | 2[" + toString(numberArray[1][1]) + "] | 1[" + toString(numberArray[1][0]) + "] | " + toString(numberArray[0][6]) + " |"};
            kalahBoard[2] = new String[]{"|    |-------+-------+-------+-------+-------+-------|    |"};
            kalahBoard[3] = new String[]{"| " + toString(numberArray[1][6]) + " | 1[" + toString(numberArray[0][0]) + "] | 2[" + toString(numberArray[0][1]) + "] | 3[" + toString(numberArray[0][2]) + "] | 4[" + toString(numberArray[0][3]) + "] | 5[" + toString(numberArray[0][4]) + "] | 6[" + toString(numberArray[0][5]) + "] | P1 |"};
            kalahBoard[4] = new String[]{"+----+-------+-------+-------+-------+-------+-------+----+"};
            //print out each row of the gameboard
            for (String[] strings : kalahBoard) {
                for (String string : strings) {
                    io.print(string + "");
                }
                io.println("");
            }
        }
        if (vertical) {
            String[][] kalahBoard = new String[12][];
            //fixed parts of the gameboard, added new features
            kalahBoard[0] = new String[]{"+---------------+"};
            kalahBoard[1] = new String[]{"|       | P2 " + toString(numberArray[1][6]) + " |"};
            kalahBoard[2] = new String[]{"+-------+-------+"};
            kalahBoard[3] = new String[]{"| 1[" + toString(numberArray[0][0]) + "] | 6[" + toString(numberArray[1][5]) + "] |"};
            kalahBoard[4] = new String[]{"| 2[" + toString(numberArray[0][1]) + "] | 5[" + toString(numberArray[1][4]) + "] |"};
            kalahBoard[5] = new String[]{"| 3[" + toString(numberArray[0][2]) + "] | 4[" + toString(numberArray[1][3]) + "] |"};
            kalahBoard[6] = new String[]{"| 4[" + toString(numberArray[0][3]) + "] | 3[" + toString(numberArray[1][2]) + "] |"};
            kalahBoard[7] = new String[]{"| 5[" + toString(numberArray[0][4]) + "] | 2[" + toString(numberArray[1][1]) + "] |"};
            kalahBoard[8] = new String[]{"| 6[" + toString(numberArray[0][5]) + "] | 1[" + toString(numberArray[1][0]) + "] |"};
            kalahBoard[9] = new String[]{"+-------+-------+"};
            kalahBoard[10] = new String[]{"| P1 " + toString(numberArray[0][6]) + " |       |"};
            kalahBoard[11] = new String[]{"+---------------+"};
            //print out each row of the gameboard
            for (String[] strings : kalahBoard) {
                for (String string : strings) {
                    io.print(string + "");
                }
                io.println("");
            }
        }
    }

    //Force integer to string conversion and judge it has one or two digits
    public static String toString(int value) {
        if (value >= 0 && value < 10) {
            return " " + value;
        }
        else {
            return Integer.toString(value);
        }
    }
}
