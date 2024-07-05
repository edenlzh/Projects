package kalah;
import com.qualitascorpus.testsupport.IO;


public class MethodInGame {
    public int[][] createNumberArrayOriginal() {
        // This is the initial state of Kalah board but in array
        int p1H1SeedNumber = 4, p1H2SeedNumber = 4, p1H3SeedNumber = 4, p1H4SeedNumber = 4, p1H5SeedNumber = 4, p1H6SeedNumber = 4;
        int p2H1SeedNumber = 4, p2H2SeedNumber = 4, p2H3SeedNumber = 4, p2H4SeedNumber = 4, p2H5SeedNumber = 4, p2H6SeedNumber = 4;
        int p1StoreNumber = 0, p2StoreNumber = 0;
        int[] p1NumberArray = {p1H1SeedNumber, p1H2SeedNumber, p1H3SeedNumber, p1H4SeedNumber, p1H5SeedNumber, p1H6SeedNumber, p1StoreNumber};
        int[] p2NumberArray = {p2H1SeedNumber, p2H2SeedNumber, p2H3SeedNumber, p2H4SeedNumber, p2H5SeedNumber, p2H6SeedNumber, p2StoreNumber};
        return new int[][]{p1NumberArray, p2NumberArray};
    }

    public int[][] createNumberArray(boolean vertical) {
        int[][] numberArrayOriginal = createNumberArrayOriginal();
        int[][] numberArray;
        if (vertical) {
            numberArray =  new int[][] {numberArrayOriginal[0], numberArrayOriginal[1], {1}};
        }
        else {
            numberArray = new int[][] {numberArrayOriginal[0], numberArrayOriginal[1], {0}};
        }
        return numberArray;
    }

    public boolean getSeedZeroCaseBooleanOne(IO io, int getSeedFromHouse, char inputChar, int[][] numberArray, boolean vertical) {
        boolean finished = false;
        GameState kalahCases = new GameState();
        if (getSeedFromHouse == 0) {
            finished = kalahCases.seedIsZeroPlayerOne(io, numberArray, getSeedFromHouse, vertical);
        }
        return finished;
    }

    public boolean getSeedZeroCaseBooleanTwo(IO io, int getSeedFromHouse, char inputChar, int[][] numberArray, boolean vertical) {
        boolean finished = false;
        GameState kalahCases = new GameState();
        if (getSeedFromHouse == 0) {
            finished = kalahCases.seedIsZeroPlayerTwo(io, numberArray, getSeedFromHouse, vertical);
        }
        return finished;
    }

    public int getCollectSeedFromInputChar (char inputChar, int[] pNumberArray) {
        // Convert char to int
        int inputInt = inputChar - '0';
        return pNumberArray[inputInt-1];
    }

    public boolean finalMoveAtStoreP1 (IO io, GameBoard kalahBoard, int finalPosition, int[][] numberArray, boolean vertical) {
        boolean finished = false;
        GameState kalahCases = new GameState();
        Player playerOne = new Player();
        while (finalPosition == 6) {
            int[] storeTotalNumberArr = kalahCases.gameFinishRoundOne(io, numberArray, vertical);
            if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                finished = true; // Use to exist the outer for loop
                break; // Use to exist the inner while loop
            }
            char inputChar = GameState.p1Prompt(io);
            if (inputChar == 'q') {
                kalahCases.printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                finished = true;
                break;
            }
            int getSeedFromHouse = getCollectSeedFromInputChar(inputChar, numberArray[0]);
            ReturnType intElementsP1Array = playerOne.p1Turn(io, numberArray, inputChar, vertical);
            numberArray = intElementsP1Array.NumArray;
            finalPosition = intElementsP1Array.finalPosition;
            finished = getSeedZeroCaseBooleanOne(io, getSeedFromHouse, inputChar, numberArray, vertical);
        }
        return finished;
    }

    public boolean finalMoveAtStoreP2 (IO io, GameBoard kalahBoard, int finalMoveIndex, int[][] numberArray, boolean vertical) {
        boolean finished = false;
        GameState kalahCases = new GameState();
        Player playerTwo = new Player();
        while (finalMoveIndex == 6) {
            int[] storeTotalNumberArr = kalahCases.gameFinishRoundTwo(io, numberArray, vertical);
            if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                finished = true;
                break;
            }
            char inputChar = GameState.p2Prompt(io);
            if (inputChar == 'q') {
                kalahCases.printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                finished = true;
                break;
            }
            int getSeedFromHouse = getCollectSeedFromInputChar(inputChar, numberArray[1]);
            ReturnType intElementsP2Array = playerTwo.p2Turn(io, numberArray, inputChar, vertical, false);
            numberArray = intElementsP2Array.NumArray;
            finalMoveIndex = intElementsP2Array.finalPosition;
            finished = getSeedZeroCaseBooleanTwo(io, getSeedFromHouse, inputChar, numberArray, vertical);
        }
        return finished;
    }

    public boolean regularMoveStepP1 (IO io, GameBoard kalahBoard, char inputChar, int[][] numberArray, boolean vertical) {
        Player playerOne = new Player();
        int getSeedFromHouse = getCollectSeedFromInputChar(inputChar, numberArray[0]);
        boolean finished = getSeedZeroCaseBooleanOne(io, getSeedFromHouse, inputChar, numberArray, vertical);
        if (getSeedFromHouse != 0) {
            ReturnType intElementsP1Array = playerOne.p1Turn(io, numberArray, inputChar, vertical);
            numberArray = intElementsP1Array.NumArray;
            int finalPosition = intElementsP1Array.finalPosition;
            if (finalPosition == 6) {
                finished = finalMoveAtStoreP1(io, kalahBoard, finalPosition, numberArray, vertical);
            }
        }
        return finished;
    }

    public boolean regularMoveStepP2 (IO io, GameBoard kalahBoard, char inputChar, int[][] numberArray, boolean vertical) {
        Player playerTwo = new Player();
        int getSeedFromHouse = getCollectSeedFromInputChar(inputChar, numberArray[1]);
        boolean finished = getSeedZeroCaseBooleanTwo(io, getSeedFromHouse, inputChar, numberArray, vertical);
        if (getSeedFromHouse != 0) {
            ReturnType intElementsP2Array = playerTwo.p2Turn(io, numberArray, inputChar, vertical, false);
            numberArray = intElementsP2Array.NumArray;
            int finalPosition = intElementsP2Array.finalPosition;
            if (finalPosition == 6) {
                finished = finalMoveAtStoreP2(io, kalahBoard, finalPosition, numberArray, vertical);
            }
        }
        return finished;
    }


    public boolean regularMoveStepRobot (IO io, GameBoard kalahBoard, int[][] numberArray, boolean isExtraMove, boolean vertical) {

        //Player robot = new Player();
        GameState kalahCases = new GameState();
        boolean finished = false;
        int[][][] combineArray = getCombineArray(io, numberArray, vertical);

        if (isExtraMove) {
            int[] storeTotalNumberArr = kalahCases.gameFinishRoundTwo(io, numberArray, vertical);
            if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                finished = true;
                return finished;
            }
        }

        boolean getOutput = false;
        for (int i=0; i<6; i++) {
            int[][] numberArrayNew = {combineArray[i][0], combineArray[i][1], numberArray[2]};
            int finalIndex = combineArray[i][2][0];
            if (finalIndex < numberArray[1].length) {
                if (finalIndex == 6) {
                    io.println("Player P2 (Robot) chooses house #" + (i+1) + " because it leads to an extra move");
                    numberArray[0] = numberArrayNew[0];
                    numberArray[1] = numberArrayNew[1];
                    numberArray[2] = numberArrayNew[2];
                    getOutput = true;
                    kalahBoard.printGameBoard(io, vertical, numberArray);
                    // One more move
                    finished = regularMoveStepRobot(io, kalahBoard, numberArray, true, vertical);

                    break;
                }
            }
        }

        if (!getOutput) {
            for (int k=0; k<6; k++) {
                int[][] numberArrayNew = {combineArray[k][0], combineArray[k][1], numberArray[2]};
                int finalIndex = combineArray[k][2][0];
                if (finalIndex < numberArray[1].length) {
                    if ((combineArray[k][0][combineArray[k][0].length-2-finalIndex] == 0) && (combineArray[k][1][finalIndex] == 0) && (combineArray[k][1][6] > numberArray[1][6]) && ((numberArray[0][numberArray[0].length-2-finalIndex] != 0) || ((numberArray[1][k]%13 > numberArray[1].length) && (numberArray[0][numberArray[0].length-2-finalIndex] == 0) && (combineArray[k][1][6] - numberArray[1][6]-2 == 1)))) {
                        io.println("Player P2 (Robot) chooses house #" + (k+1) + " because it leads to a capture");
                        numberArray[0] = numberArrayNew[0];
                        numberArray[1] = numberArrayNew[1];
                        numberArray[2] = numberArrayNew[2];
                        getOutput = true;
                        kalahBoard.printGameBoard(io, vertical, numberArray);
                        break;
                    }
                }
            }
        }

        if (!getOutput) {
            for (int j=0; j<6; j++) {
                if (numberArray[1][j] != 0) {
                    io.println("Player P2 (Robot) chooses house #" + (j+1) + " because it is the first legal move");
                    int[][] numberArrayLegal = {combineArray[j][0], combineArray[j][1], numberArray[2]};
                    numberArray[0] = numberArrayLegal[0];
                    numberArray[1] = numberArrayLegal[1];
                    numberArray[2] = numberArrayLegal[2];
                    kalahBoard.printGameBoard(io, vertical, numberArray);
                    break;
                }
            }
        }
        return finished;

    }

    public int[][][] getCombineArray (IO io, int[][] numberArray, boolean vertical) {
        Player robot = new Player();
        int[][][] combineArray = {{},{},{},{},{},{}};
        int[][] temp = clone(numberArray);
        int index=0;
        while (index<6) {
            int[][] UseNumArray = clone(temp);
            char possibleIndexChar = (char)((index+1) + '0');
            // The last parameter for playerTwoRound is the boolean bmf
            ReturnType intElementsP2Array = robot.p2Turn(io, UseNumArray, possibleIndexChar, vertical, true);
            UseNumArray = intElementsP2Array.NumArray;
            int finalPosition = intElementsP2Array.finalPosition;
            if (temp[1][index] == 0) {
                finalPosition = index;
            }
            int[][] ArrayComponent =  {UseNumArray[0], UseNumArray[1], {finalPosition}};
            combineArray[index] = ArrayComponent;
            index++;
        }
        return combineArray;
    }

    public int[][] clone(int[][] a) {
        int[][] b = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i].clone();
        }
        return b;
    }
}
