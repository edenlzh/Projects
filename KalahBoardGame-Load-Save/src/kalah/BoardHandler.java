package kalah;

import com.qualitascorpus.testsupport.IO;
import java.util.ArrayList;

/**
 * Originator Class for the Memento design pattern
 *
 * This class contains the Board whilst also carrying out any actions and changes of state
 * to the Board indirectly. It contains both Players whose Pit's make up the playing Board.
 */
public class BoardHandler {
    private Player _playerOne;
    private Player _playerTwo;
    private GameBoard _currentBoardState;
    private int BOARDSIZE;
    private int NUMHOUSES;
    private int P1STOREINDEX;
    private int P2STOREINDEX;

    /**
     * Default Constructor
     */
    BoardHandler() {
        _playerOne = new Player();
        _playerTwo = new Player();
        _currentBoardState = initialiseBoard();
        P1STOREINDEX = _currentBoardState.getP1STOREINDEX();
        P2STOREINDEX = _currentBoardState.getP2STOREINDEX();
    }

    /**
     * Initialises the state of a new Board
     * @return the new Board
     */
    private GameBoard initialiseBoard() {
        ArrayList<Pit> newBoard = new ArrayList<>();
        newBoard.addAll(_playerOne.getPlayerPits());
        newBoard.addAll(_playerTwo.getPlayerPits());
        BOARDSIZE = newBoard.size();
        NUMHOUSES = _playerOne.getNUMHOUSES();
        return new GameBoard(newBoard);
    }

    /**
     * Starts the Player's turn and handles finalising the move if a valid move is taken.
     * If an invalid house number is selected, the user is prompted for another turn.
     * @param isPlayerOneTurn - TRUE if P1s turn
     * @param selectedHouse the house # entered by the user
     * @param io
     * @return TRUE for p1 turn next, FALSE for p2
     */
    public boolean startPlayerTurn(boolean isPlayerOneTurn, int selectedHouse, IO io) {
        int startHouseIndex = isPlayerOneTurn ? (selectedHouse - 1) : (selectedHouse + NUMHOUSES);
        int numSeedsToSow = _currentBoardState.getPit(startHouseIndex).getNumSeed();
        int finalPitLanded = -1;
        boolean isPlayerOneNext = isPlayerOneTurn;

        if(!isValidMove(numSeedsToSow)) {
            io.println("House is empty. Move again.");
        } else {
            finalPitLanded = sowSeeds(numSeedsToSow, startHouseIndex, isPlayerOneTurn);
            isPlayerOneNext = performEndTurnChecks(finalPitLanded, isPlayerOneTurn);
        }

        return isPlayerOneNext;
    }

    /**
     * Sets the state of the board by sowing the seeds for each turn. Takes seeds from
     * start House and distributes through each applicable Pit until no seeds remain.
     * @param numSeedsToSow - seeds from selectedHouse
     * @param startHouseIndex - index of selectedHouse (changes depending on which Players turn it is)
     * @param isPlayerOneTurn - TRUE if p1 turn
     * @return index of Pit where final seed is placed
     */
    private int sowSeeds(int numSeedsToSow, int startHouseIndex, boolean isPlayerOneTurn) {
        int currentPos = startHouseIndex;
        int movesMade = 1;
        _currentBoardState.getPit(startHouseIndex).emptyPit();
        while(movesMade <= numSeedsToSow) {
            if(passThroughOppStore(startHouseIndex, movesMade, isPlayerOneTurn)) {
                //skip to next Pit and account for extra move made
                movesMade++;
                numSeedsToSow++;
            }
            //end of board reached, go back to start
            if(startHouseIndex + movesMade == BOARDSIZE)
                startHouseIndex -= (startHouseIndex + movesMade);
            _currentBoardState.getPit(startHouseIndex + movesMade).addSeed(1);
            movesMade++;
            currentPos = startHouseIndex + movesMade - 1;
        }
        return currentPos;
    }

    /**
     * Checks for any special moves and conditions that may occur at end of turn
     * 1. Lands in own store
     * 2. P1 can capture
     * 3. P2 can capture
     * @param finalPit - index of Pit where final seed landed
     * @param isPlayerOneTurn
     * @return - TRUE if p1 next
     */
    private boolean performEndTurnChecks(int finalPit, boolean isPlayerOneTurn) {
        boolean isP1next = !isPlayerOneTurn; //by default, change turn

        if(landedInOwnStore(finalPit, isPlayerOneTurn))
            isP1next = !isP1next;
        else if(p1CanCapture(finalPit, isPlayerOneTurn))
            captureSeeds(finalPit, isPlayerOneTurn);
        else if(p2CanCapture(finalPit, isPlayerOneTurn))
            captureSeeds(finalPit, isPlayerOneTurn);

        return isP1next;
    }

    /**
     * Checks that user enters a valid move, house is not empty
     * @param numSeeds - num of seeds in selectedHouse
     * @return TRUE if move is valid
     */
    private boolean isValidMove(int numSeeds) {
        return !(numSeeds == 0);
    }

    /**
     * @returns current state of playing Board
     */
    public GameBoard getCurrentBoardState() {
        return _currentBoardState;
    }

    /**
     * Checks if move passes through oppositions Store, in which case skip over it
     * @param startHouseIndex - where move started
     * @param movesMade - moves made during turn
     * @param isPlayerOneTurn - TRUE if P1 turn
     * @return - TRUE if current position is opposition's Store
     */
    private boolean passThroughOppStore(int startHouseIndex, int movesMade, boolean isPlayerOneTurn) {
        boolean p1PassesP2Store = isPlayerOneTurn && (startHouseIndex + movesMade == P2STOREINDEX);
        boolean p2PassesP2Store = !isPlayerOneTurn && (startHouseIndex + movesMade  == P1STOREINDEX);
        return (p1PassesP2Store || p2PassesP2Store);
    }

    /**
     * Checks if a Player ends their turn in their own Store
     * @param currentPos, index of Pit landed in
     * @param isPlayerOneTurn - TRUE if p1 turn
     * @return TRUE if landed in own store
     */
    private boolean landedInOwnStore(int currentPos, boolean isPlayerOneTurn) {
        boolean p1LandedOwnStore = isPlayerOneTurn && (currentPos == P1STOREINDEX);
        boolean p2LandedOwnStore = !isPlayerOneTurn && (currentPos == P2STOREINDEX);
        return p1LandedOwnStore || p2LandedOwnStore;
    }

    /**
     * Checks if P1 can capture at the end of their turn. To do this, they must meet the following:
     * 1. Lands in their (previously) own EMPTY House
     * 2. The opposing House must not be empty
     * @param currentPos - index of Pit where turn ended
     * @param isPlayerOneTurn - TRUE if p1 turn
     * @return - TRUE if p1 can capture
     */
    private boolean p1CanCapture(int currentPos, boolean isPlayerOneTurn) {
        boolean p1LandsOwnEmptyHouse = (currentPos < P1STOREINDEX) && _currentBoardState.getPit(currentPos).getNumSeed() == 1;
        boolean oppHouseNotEmpty = _currentBoardState.getPit(getOpposingHouseIndex(currentPos)).getNumSeed() != 0;
        return (isPlayerOneTurn && p1LandsOwnEmptyHouse && oppHouseNotEmpty);
    }

    /**
     * Checks if P2 can capture at the end of their turn. To do this, they must meet the following:
     * 1. Lands in their (previously) own EMPTY House
     * 2. The opposing House must not be empty
     * @param currentPos - index of Pit where turn ended
     * @param isPlayerOneTurn - FALSE if p2 turn
     * @return - TRUE if p2 can capture
     */
    private boolean p2CanCapture(int currentPos, boolean isPlayerOneTurn) {
        boolean p2LandsOwnEmptyHouse = (currentPos > P1STOREINDEX) && _currentBoardState.getPit(currentPos).getNumSeed() == 1;
        boolean oppHouseNotEmpty = _currentBoardState.getPit(getOpposingHouseIndex(currentPos)).getNumSeed() != 0;
        return (!isPlayerOneTurn && p2LandsOwnEmptyHouse && oppHouseNotEmpty);
    }

    /**
     * @param currentPos - the current position on the Board
     * @return - the opposing House, directly opposite currentPos
     */
    private int getOpposingHouseIndex(int currentPos) {
        return -currentPos + (NUMHOUSES * 2);
    }

    /**
     * Both the seed placed in the current Player's currentPos and the opposing seeds are 'captured' into
     * the current Player's store
     * @param currentPos - index of currentPos Pit on Board
     * @param isPlayerOneTurn
     */
    private void captureSeeds(int currentPos, boolean isPlayerOneTurn) {
        int oppSeeds = _currentBoardState.getPit(getOpposingHouseIndex(currentPos)).captureSeed();
        int ownSeed = _currentBoardState.getPit(currentPos).captureSeed();
        if(isPlayerOneTurn)
            _currentBoardState.getPit(P1STOREINDEX).addSeed(oppSeeds + ownSeed);
        else
            _currentBoardState.getPit(P2STOREINDEX).addSeed(oppSeeds + ownSeed);
    }

    /**
     * Checks if the Board is empty. Either:
     * 1. P1 has no seeds left to play
     * 2. P2 has no seeds left to play
     * @param isPlayerOneTurn
     * @return TRUE if a Player's board is empty
     */
    public boolean checkBoardEmpty(boolean isPlayerOneTurn) {
        int p1seedsRemaining = 0;
        int p2seedsRemaining = 0;

        for(int i = 0; i < NUMHOUSES; i++)   {
            if(i != P1STOREINDEX)
                p1seedsRemaining += _playerOne.getPlayerPits().get(i).getNumSeed();
            if(i != P2STOREINDEX)
                p2seedsRemaining += _playerTwo.getPlayerPits().get(i).getNumSeed();
        }

        return ((p1seedsRemaining == 0 && isPlayerOneTurn) || (p2seedsRemaining == 0 && !isPlayerOneTurn));
    }

    /**
     * Displays the current state of the Board
     * @param io
     */
    public void displayBoard(IO io) {
        _currentBoardState.displayBoard(io);
    }

    /**
     * Retrieve the final scores of both Player's
     * @return - both Player's scores
     */
    public int[] getFinalScores() {
        _playerOne.calcScore();
        _playerTwo.calcScore();

        return new int[]{_playerOne.getScore(), _playerTwo.getScore()};
    }

    /**
     * Saves the state of the current Board (MEMENTO DESIGN) into a new Board
     * @param isP1Turn
     * @return
     */
    public GameBoard saveBoardState(boolean isP1Turn) {
        ArrayList<Pit> copyOfCurrentBoard = new ArrayList<>();
        _playerOne = new Player();
        _playerTwo = new Player();

        for(int i = 0; i <= NUMHOUSES; i++) {
            _playerOne.getPlayerPits().get(i).loadPit(_currentBoardState.getPit(i).getNumSeed());
            _playerTwo.getPlayerPits().get(i).loadPit(_currentBoardState.getPit(i + NUMHOUSES  + 1).getNumSeed());
        }
        copyOfCurrentBoard.addAll(_playerOne.getPlayerPits());
        copyOfCurrentBoard.addAll(_playerTwo.getPlayerPits());

        return new GameBoard(copyOfCurrentBoard, isP1Turn);
    }

    /**
     * Loads the recently saved Board into the current Board, if a saved Board exists
     * @param recentlySavedBoard
     * @return
     */
    public boolean loadRecentBoardState(GameBoard recentlySavedBoard) {
        boolean saveExists = false;
        if(!(recentlySavedBoard == null)) {
            saveExists = true;
            _currentBoardState = recentlySavedBoard;
        }
        return saveExists;
    }
}

