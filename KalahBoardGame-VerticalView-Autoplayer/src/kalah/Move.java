package kalah;

import com.qualitascorpus.testsupport.IO;



/**
 * This class is the Move class, which is in charge of the moving
 * in the back of the gameboard. The methods return an integer
 * lastPosition, two int[] p1NumberArray and p2NumberArray.
 */

public class Move {
    public ReturnType moveOfP1ReturnFinalPosition(IO io, int[][] numberArray, int inputPosition, boolean vertical) {

        int p2Store = numberArray[1][numberArray[1].length-1];
        int[] reactiveArray = createReactiveArr(numberArray, "P1");
        int getSeedFromHouse = reactiveArray[inputPosition-1]; // get seeds in the selected house
        reactiveArray[inputPosition-1] = 0;
        ReturnType legalMoveArray = legalMove(getSeedFromHouse, inputPosition, reactiveArray);
        int lastMovePosition = legalMoveArray.finalPosition;
        reactiveArray = legalMoveArray.numArrayOneD;
        numberArray = convertOneDArrToTwoDArr(reactiveArray, numberArray, "P2");
        numberArray[1][numberArray[1].length-1]= p2Store;
        specialCaseKalahP1(io, vertical, lastMovePosition, numberArray, getSeedFromHouse);
        return new ReturnType(lastMovePosition, numberArray);

    }

    public ReturnType moveOfP2ReturnFinalPosition(IO io, int[][] numberArray, int inputPosition, boolean vertical, boolean bmf) {

        int p1Store = numberArray[0][numberArray[0].length-1];
        int[] reactiveArray = createReactiveArr(numberArray, "P2");
        int getSeedFromHouse = reactiveArray[inputPosition-1];
        reactiveArray[inputPosition-1] = 0;
        ReturnType legalMoveArray = legalMove(getSeedFromHouse, inputPosition, reactiveArray);
        int lastMovePosition = legalMoveArray.finalPosition;
        reactiveArray = legalMoveArray.numArrayOneD;
        numberArray = convertOneDArrToTwoDArr(reactiveArray, numberArray, "P1");
        numberArray[0][numberArray[0].length-1]= p1Store;
        if ((numberArray[2][0] == 0) && (bmf)) {
            specialCaseKalahRobot(lastMovePosition, numberArray);

        }
        else  {
            specialCaseKalahP2(io, vertical, lastMovePosition, numberArray, getSeedFromHouse);

        }
        return new ReturnType(lastMovePosition, numberArray);
    }

    public int[] createReactiveArr(int[][] numberArray, String playerNum) {
        int[] reactiveArray = new int[13];
        if (playerNum.equals("P1")) {
            reactiveArray = new int[]{numberArray[0][0], numberArray[0][1], numberArray[0][2], numberArray[0][3], numberArray[0][4], numberArray[0][5], numberArray[0][6], numberArray[1][0], numberArray[1][1], numberArray[1][2], numberArray[1][3], numberArray[1][4], numberArray[1][5]};
            //initial1 [4,4,4,4,4,4,0,4,4,4,4,4,4] length=13
        }
        else if (playerNum.equals("P2")) {
            reactiveArray = new int[]{numberArray[1][0], numberArray[1][1], numberArray[1][2], numberArray[1][3], numberArray[1][4], numberArray[1][5], numberArray[1][6], numberArray[0][0], numberArray[0][1], numberArray[0][2], numberArray[0][3], numberArray[0][4], numberArray[0][5]};
        }
        return reactiveArray;
    }

    // while the collected seed is not 0, plant 1 each house anti-clockwise, including the player's store (p1 houses + p1 store + p2 house; if out of index then start again at the P1's house 1)
    public ReturnType legalMove(int getSeedFromHouse, int inputPosition, int[] reactiveArray) {
        int lastMovePositon = 0;
        if ((getSeedFromHouse+inputPosition)<=reactiveArray.length){ //if inputInt=6, collectedSeed<=7
            for (int i=0; i<getSeedFromHouse; i++){
                reactiveArray[inputPosition+i]++;
                lastMovePositon = inputPosition+i;
            }
        }
        else if ((reactiveArray.length<(getSeedFromHouse+inputPosition)) && ((getSeedFromHouse+inputPosition)<=(reactiveArray.length*2))){ // if inputInt=6, 8<=collectSeed<=20
            for (int i=0; i<reactiveArray.length-inputPosition; i++){
                reactiveArray[inputPosition+i]++;
            }
            for (int j=0; j<getSeedFromHouse-(reactiveArray.length-inputPosition); j++){
                reactiveArray[j]++;
                lastMovePositon = j;
            }
        }
        else if ((reactiveArray.length*2<(getSeedFromHouse+inputPosition)) && ((getSeedFromHouse+inputPosition)<=(reactiveArray.length*3))){ // if inputInt=6, 21<=collectSeed<=33
            for (int i=0; i<reactiveArray.length-inputPosition; i++){
                reactiveArray[inputPosition+i]++;
            }
            for (int j=0; j<reactiveArray.length; j++){
                reactiveArray[j]++;
            }
            for (int k=0; k<getSeedFromHouse-(reactiveArray.length*2-inputPosition); k++){
                reactiveArray[k]++;
                lastMovePositon = k;
            }
        }
        return new ReturnType(lastMovePositon, reactiveArray);
    }

    // Convert reactiveArray (int[]) back into numberArray (int[][])
    public int[][] convertOneDArrToTwoDArr(int[] reactiveArray, int[][] numberArray, String pStore) {
        if (pStore.equals("P1")) {
            for (int position=0; position<reactiveArray.length; position++) {
                if (position < numberArray[0].length) {
                    numberArray[1][position] = reactiveArray[position];
                }
                else {
                    numberArray[0][position-numberArray[0].length] = reactiveArray[position];
                }
            }
        }
        else if (pStore.equals("P2")) {
            for (int position=0; position<reactiveArray.length; position++) {
                if (position < numberArray[0].length) {
                    numberArray[0][position] = reactiveArray[position];
                }
                else {
                    numberArray[1][position-numberArray[0].length] = reactiveArray[position];
                }
            }
        }
        return numberArray;
    }

    // Special case - if a move ends in player's house that is empty,
    // the player gets that seed and all seeds in the opposing house.
    private int[][] specialCaseKalahP1(IO io, boolean vertical, int lastMovePosition, int[][] numberArray, int getSeedFromHouse) {
        GameBoard kalahBoard = new GameBoard();
        if ((lastMovePosition != 6) && (lastMovePosition < numberArray[0].length) && (numberArray[0][lastMovePosition] == 1)) {
            if (numberArray[1][numberArray[1].length-2-lastMovePosition] > 0) {
                int p1Seed = numberArray[0][lastMovePosition];
                int p2Seed = numberArray[1][numberArray[1].length-2-lastMovePosition];
                numberArray[0][lastMovePosition] = 0;
                numberArray[1][numberArray[1].length-2-lastMovePosition] = 0;
                numberArray[0][numberArray[0].length-1] = numberArray[0][numberArray[0].length-1] + p1Seed + p2Seed;
                kalahBoard.printGameBoard(io, vertical, numberArray);
            }
            else {
                kalahBoard.printGameBoard(io, vertical, numberArray);
            }
        }
        // In case of empty seed, straight print "House empty. Move again." and then print the existing kalah board.
        else if (getSeedFromHouse != 0) {
            kalahBoard.printGameBoard(io, vertical, numberArray);
        }
        return numberArray;
    }

    private int[][] specialCaseKalahP2(IO io, boolean vertical, int lastMovePosition, int[][] numberArray, int getSeedFromHouse) {
        GameBoard kalahBoard = new GameBoard();
        if ((lastMovePosition!= 6) && (lastMovePosition < numberArray[1].length) && (numberArray[1][lastMovePosition] == 1)) {
            if (numberArray[0][numberArray[0].length-2-lastMovePosition] > 0) {
                int p2Seed = numberArray[1][lastMovePosition];
                int p1Seed = numberArray[0][numberArray[0].length-2-lastMovePosition];
                numberArray[1][lastMovePosition] = 0;
                numberArray[0][numberArray[0].length-2-lastMovePosition] = 0;
                numberArray[1][numberArray[1].length-1] = numberArray[1][numberArray[0].length-1] + p1Seed + p2Seed;
                kalahBoard.printGameBoard(io, vertical, numberArray);
            }
            else {
                kalahBoard.printGameBoard(io, vertical, numberArray);
            }
        }
        else if (getSeedFromHouse != 0) {
            kalahBoard.printGameBoard(io, vertical, numberArray);
        }
        return numberArray;
    }

    private int[][] specialCaseKalahRobot(int lastMovePosition, int[][] numberArray) {
        //KalahBoard kalahBoard = new KalahBoard();
        if ((lastMovePosition != 6) && (lastMovePosition < numberArray[1].length) && (numberArray[1][lastMovePosition] == 1)) {
            if (numberArray[0][numberArray[0].length-2-lastMovePosition] > 0) {
                int p2Seed = numberArray[1][lastMovePosition];
                int p1Seed = numberArray[0][numberArray[0].length-2-lastMovePosition];
                numberArray[1][lastMovePosition] = 0;
                numberArray[0][numberArray[0].length-2-lastMovePosition] = 0;
                numberArray[1][numberArray[1].length-1] = numberArray[1][numberArray[0].length-1] + p1Seed + p2Seed;
            }
        }
        return numberArray;
    }
}