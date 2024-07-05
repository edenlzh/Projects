package kalah;
import com.qualitascorpus.testsupport.IO;

public class GameRound {
    public void P1P2round(IO io, boolean vertical) {
        GameBoard kalahBoard = new GameBoard();
        GameState kalahCases = new GameState();
        MethodInGame mig12 = new MethodInGame();

        int[][] numberArray = mig12.createNumberArray(vertical);
        kalahBoard.printGameBoard(io, vertical, numberArray);
        boolean finished = false; // Use with break; to exist for loop when within a while loop
        for (int round=0; round < 100000 && !finished; round++) {
            if (round % 2 == 0) { // P1 round
                int[] storeTotalNumberArr = kalahCases.gameFinishRoundOne(io, numberArray, vertical);
                if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                    break;
                }
                char inputChar = GameState.p1Prompt(io);
                if (inputChar == 'q') {
                    kalahCases.printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                    break;
                }
                if (inputChar == '1' || inputChar == '2' || inputChar == '3' || inputChar == '4' || inputChar == '5' || inputChar == '6') {
                    // do the move
                    finished = mig12.regularMoveStepP1(io, kalahBoard, inputChar, numberArray, vertical);
                }
            }
            else if (round % 2 == 1) { //P2 round
                int[] storeTotalNumberArr = kalahCases.gameFinishRoundTwo(io, numberArray, vertical);
                if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                    break;
                }
                char inputChar = GameState.p2Prompt(io);
                if (inputChar == 'q') {
                    kalahCases.printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                    break;
                }
                if (inputChar == '1' || inputChar == '2' || inputChar == '3' || inputChar == '4' || inputChar == '5' || inputChar == '6') {
                    // do the move
                    finished = mig12.regularMoveStepP2(io, kalahBoard, inputChar, numberArray, vertical);
                }
            }
        }
    }

    public void P1RobotRound(IO io, boolean vertical) {
        GameBoard kalahBoard = new GameBoard();
        GameState kalahCases = new GameState();
        MethodInGame mig1R = new MethodInGame();

        int[][] numberArray = mig1R.createNumberArray(vertical);

        kalahBoard.printGameBoard(io, vertical, numberArray);
        boolean finished = false; // Use with break; to exist for loop when within a while loop
        for (int round=0; round < 100000 && !finished; round++) {
            if (round % 2 == 0) { // P1 round
                int[] storeTotalNumberArr = kalahCases.gameFinishRoundOne(io, numberArray, vertical);
                if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                    break;
                }
                char inputChar = GameState.p1Prompt(io);
                if (inputChar == 'q') {
                    kalahCases.printGameOverAndBoard(io, kalahBoard, numberArray, vertical);
                    break;
                }
                if (inputChar == '1' || inputChar == '2' || inputChar == '3' || inputChar == '4' || inputChar == '5' || inputChar == '6') {
                    // do the move
                    finished = mig1R.regularMoveStepP1(io, kalahBoard, inputChar, numberArray, vertical);
                }
            }
            else if (round % 2 == 1) { //robot round
                int[] storeTotalNumberArr = kalahCases.gameFinishRoundTwo(io, numberArray, vertical);
                if ((storeTotalNumberArr[0] != 0) && (storeTotalNumberArr[1] != 0) ){
                    break;
                }
                finished = mig1R.regularMoveStepRobot(io, kalahBoard, numberArray, false, vertical);

            }
        }
    }
}
