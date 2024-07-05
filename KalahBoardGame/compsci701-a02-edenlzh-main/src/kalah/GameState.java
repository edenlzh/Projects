package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * This class contains the basic prompts and
 * the situations of winning and empty house.
 * Added vertical and bmf.
 */
public class GameState {

    public static char p1Prompt(IO io) {
        String promptForP1 = "Player P1's turn - Specify house number or 'q' to quit: ";
        String inputStringForP1 = io.readFromKeyboard(promptForP1);
        return inputStringForP1.charAt(0);
    }
    public static char p2Prompt(IO io) {
        String promptForP2 = "Player P2's turn - Specify house number or 'q' to quit: ";
        String inputStringForP2 = io.readFromKeyboard(promptForP2);
        return inputStringForP2.charAt(0);
    }

    public int[] gameFinishRoundOne(IO io, int[][] numberArray, boolean vertical) {
        GameBoard kalahBoard = new GameBoard();
        int[] storeTotalNumberArr = new int[2];
        // storeTotalNumberArr = {p1StoreTotalNumber, p1StoreTotalNumber} = {0, 0} for initialize
        if (numberArray[0][0]==0 && numberArray[0][1]==0 && numberArray[0][2]==0 && numberArray[0][3]==0 && numberArray[0][4]==0 && numberArray[0][5]==0) {
            printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
            storeTotalNumberArr[0] = storeNumberOne(numberArray[0]);
            storeTotalNumberArr[1] = storeNumberTwo(numberArray[1]);
            printStoreNumber(io, storeTotalNumberArr);
        }
        printWinPlayer(io, storeTotalNumberArr);
        return storeTotalNumberArr;
    }

    public int[] gameFinishRoundTwo(IO io, int[][] numberArray, boolean vertical) {
        GameBoard kalahBoard = new GameBoard();
        int[] storeTotalNumberArr = new int[2];
        // storeTotalNumberArr = {p1StoreTotalNumber, p1StoreTotalNumber} = {0, 0} for initialize
        if (numberArray[1][0]==0 && numberArray[1][1]==0 && numberArray[1][2]==0 && numberArray[1][3]==0 && numberArray[1][4]==0 && numberArray[1][5]==0) {
            printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
            storeTotalNumberArr[1] = storeNumberOne(numberArray[1]);
            storeTotalNumberArr[0] = storeNumberTwo(numberArray[0]);
            printStoreNumber(io, storeTotalNumberArr);
        }
        printWinPlayer(io, storeTotalNumberArr);
        return storeTotalNumberArr;
    }

    public boolean seedIsZeroPlayerOne(IO io, int[][] numberArray, int getSeedFromHouse, boolean vertical) {
        Player playerOne = new Player();
        GameBoard kalahBoard = new GameBoard();
        boolean finished = false;
        while (getSeedFromHouse == 0) {
            printHouseEmptyAndBoard(io, kalahBoard, numberArray, vertical);
            // one more round
            char inputChar = p1Prompt(io);
            if (inputChar == 'q') {
                printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                finished = true;
                break;
            }
            getSeedFromHouse = getCollectSeedFromHouse(inputChar, numberArray, "P1");
            ReturnType intElementsP1Array = playerOne.p1Turn(io, numberArray, inputChar, vertical);
            assignNumberArray(numberArray, intElementsP1Array);
        }
        return finished;
    }

    public boolean seedIsZeroPlayerTwo(IO io, int[][] numberArray, int getSeedFromHouse, boolean vertical) {
        Player playerTwo = new Player();
        GameBoard kalahBoard = new GameBoard();
        boolean finished = false;
        while (getSeedFromHouse == 0) {
            printHouseEmptyAndBoard(io, kalahBoard, numberArray, vertical);
            // one more round
            char inputChar = p2Prompt(io);
            getSeedFromHouse = getCollectSeedFromHouse(inputChar, numberArray, "P2");
            if (inputChar == 'q') {
                printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                finished = true;
                break;
            }
            ReturnType intElementsP2Array = playerTwo.p2Turn(io, numberArray, inputChar, vertical, false);
            assignNumberArray(numberArray, intElementsP2Array);
        }
        return finished;
    }

    public static int storeNumberOne(int[] arrayFirst) {
        int storeTotalNumber;
        storeTotalNumber = arrayFirst[6];
        return storeTotalNumber;
    }

    public static int storeNumberTwo(int[] arraySecond) {
        int storeTotalNumber;
        storeTotalNumber = arraySecond[6] + arraySecond[0] + arraySecond[1] + arraySecond[2] + arraySecond[3] + arraySecond[4] + arraySecond[5];
        return storeTotalNumber;
    }

    private void printStoreNumber(IO io, int[] storeTotalNumberArr) {
        io.println("	player 1:" + storeTotalNumberArr[0]);
        io.println("	player 2:" + storeTotalNumberArr[1]);
    }

    public void printGameOverAndBoard(IO io, GameBoard kalahBoard, int[][] numberArray, boolean vertical) {
        io.println("Game over");
        kalahBoard.printGameBoard(io, vertical, numberArray);
    }

    public void printHouseEmptyAndBoard(IO io, GameBoard kalahBoard, int[][] numberArray, boolean vertical) {
        io.println("House is empty. Move again.");
        kalahBoard.printGameBoard(io, vertical, numberArray);
    }

    private void printWinPlayer(IO io, int[] storeTotalNumberArr) {
        if (storeTotalNumberArr[1] > storeTotalNumberArr[0]) {
            io.println("Player 2 wins!");
        }
        else if (storeTotalNumberArr[0] > storeTotalNumberArr[1]) {
            io.println("Player 1 wins!");
        }
        else if (storeTotalNumberArr[0] != 0) {
            io.println("A tie!");
        }
    }

    private static void assignNumberArray(int[][] numberArray, ReturnType intElementsP1Array){
        numberArray[0] = intElementsP1Array.NumArray[0];
        numberArray[1] = intElementsP1Array.NumArray[1];
    }

    public int getCollectSeedFromHouse(char inputChar, int[][] numberArray, String round) {
        int inputInt = inputChar - '0';
        int getSeedFromHouse = 0;
        if (round.equals("P1")) {
            getSeedFromHouse = numberArray[0][inputInt-1];
        }
        else if (round.equals("P2")) {
            getSeedFromHouse = numberArray[1][inputInt-1];
        }
        return getSeedFromHouse;
    }
}
