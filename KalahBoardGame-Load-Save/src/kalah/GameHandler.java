package kalah;

import com.qualitascorpus.testsupport.IO;

/**
 * Receiver Class for the Command design pattern
 *
 * This class controls the state of the Game, storing all variables that describe
 * the current state of the game as well as containing a BoardHandler which controls
 * the State of the Board.
 */
public class GameHandler {
    private boolean _playing;
    private boolean _isPlayerOneTurn;
    private boolean _userQuit;
    private BoardHandler _boardHandler;
    private SavedBoard _savedBoardStates;
    private final ActionInvoker _gameActionInvoker;

    GameHandler() {
        _gameActionInvoker = new ActionInvoker();
    }

    /**
     * Initiates a new game state, resetting everything
     * @return new Playing Board
     */
    public GameBoard initialiseNewGameState() {
        _boardHandler = new BoardHandler();
        _savedBoardStates = new SavedBoard();
        _playing = true;
        _isPlayerOneTurn = true;
        _userQuit = false;
        return _boardHandler.getCurrentBoardState();
    }

    /**
     * @return TRUE if game is still in play
     */
    public boolean isPlaying() { return _playing; }

    /**
     * Gets the Player's input for each turn. Calls for input validation,
     * returned int not required if non-integer input is entered.
     * @param io
     * @return the integer entered
     */
    public int getPlayerTurnInput(IO io) {
        int selectedHouse;
        String playerInput;
        printTurnOptions(io);
        playerInput = io.readFromKeyboard("Choice:");
        selectedHouse = validatePlayerInput(playerInput, io);

        return selectedHouse;
    }

    /**
     * Validates the Player input, checking that a valid integer is input or one of the
     * appropriate menu options. Calls to execute the appropriate Commands.
     * @param playerInput - from their turn
     * @param io
     * @return - the Player's selected House
     */
    private int validatePlayerInput(String playerInput, IO io) {
        int selectedHouse = -1;
        if(isInputInt(playerInput)) {
            selectedHouse = Integer.parseInt(playerInput);
        } else if(playerInput.equalsIgnoreCase("N")) { //New Game
            _gameActionInvoker.setCommand(new NewCommand(this, io));
        } else if(playerInput.equalsIgnoreCase("S")) { //Save Game
            _gameActionInvoker.setCommand(new SaveCommand(this, io));
        } else if(playerInput.equalsIgnoreCase("L")) { //Load Game
            _gameActionInvoker.setCommand(new LoadCommand(this, io));
        } else if(playerInput.equalsIgnoreCase("Q")) { //Quit Game
            _gameActionInvoker.setCommand(new QuitCommand(this, io));
        } //no requirement to handle other inputs
        if(selectedHouse == -1)
            _gameActionInvoker.executeCommand();

        return selectedHouse;
    }

    /**
     * Displays the input options to the Player each turn
     * @param io
     */
    private void printTurnOptions(IO io) {
        io.println("Player P" + (_isPlayerOneTurn ? "1" : "2"));
        io.println("    (1-6) - house number for move");
        io.println("    N - New game");
        io.println("    S - Save game");
        io.println("    L - Load game");
        io.println("    q - Quit");
    }

    /**
     * Checks if the String input by the Player is an Integer,
     * signifies they have selected a House on the playing Board.
     * @param s - the Player input
     * @return
     */
    private boolean isInputInt(String s) {
        boolean isInputInt = true;
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            isInputInt = false;
        }
        return isInputInt;
    }

    /**
     * Starts a new game
     * @param io
     */
    public void newGame(IO io) {
        initialiseNewGameState();
        _boardHandler.displayBoard(io);
    }

    /**
     * Quits the game abruptly
     * @param io
     */
    public void userHasQuit(IO io) {
        io.println("Game over");
        getBoardDisplay(io);
        _playing = false;
        _userQuit = true;
    }

    /**
     * Checks if the user has quit the game
     * @return
     */
    public boolean hasUserQuit() {
        return _userQuit;
    }

    /**
     * Ends the game by letting the users know and getting the final scores.
     * Used for natural end to game
     * @param io
     */
    public void endGame(IO io) {
        io.println("Game over");
        getBoardDisplay(io);
        findWinner(io);

        _playing = false;
    }

    /**
     * Saves the game by calling to save the current Board state to the list of saved Boards.
     * @param io
     */
    public void saveGame(IO io) {
        _savedBoardStates.addSavedBoardState(_boardHandler.saveBoardState(_isPlayerOneTurn));
        _boardHandler.displayBoard(io);
    }

    /**
     * Loads the most recently saved Board into the current Board state, if there is a
     * saved Board to be loaded. Informs the user otherwise.
     * @param io
     */
    public void loadGame(IO io) {
        if(!(_boardHandler.loadRecentBoardState(_savedBoardStates.getRecentlySavedBoard())))
            io.println("No saved game");
        else
            _isPlayerOneTurn = _savedBoardStates.getRecentlySavedBoard().getIsP1Turn();
        _boardHandler.displayBoard(io);
    }

    /**
     * Starts each turn. Calls to get the input then at the end of each turn checks if the Board
     * is empty, signifying the end of game/final turn to be played.
     * @param selectedHouse
     * @param io
     */
    public void playTurn(int selectedHouse, IO io) {
        _isPlayerOneTurn = _boardHandler.startPlayerTurn(_isPlayerOneTurn, selectedHouse, io);
        _playing = !(_boardHandler.checkBoardEmpty(_isPlayerOneTurn));
        _boardHandler.displayBoard(io);
    }

    /**
     * Checks if the game is over, i.e. Board is empty
     * @return
     */
    public boolean isGameOver() {
        return _boardHandler.checkBoardEmpty(_isPlayerOneTurn);
    }

    /**
     * Displays the Board
     * @param io
     */
    public void getBoardDisplay(IO io) {
        _boardHandler.displayBoard(io);
    }

    /**
     * Finds the winner of the game
     * @param io
     */
    public void findWinner(IO io) {
        int[] finalScores = _boardHandler.getFinalScores();
        GameResults didPlayerOne;
        displayFinalScores(io, finalScores);

        //Is P1 score more than P2?
        if(finalScores[0] > finalScores[1])
            didPlayerOne = GameResults.WIN;
        else if(finalScores[0] < finalScores[1]) //P1 less than P2
            didPlayerOne = GameResults.LOSS;
        else //draw
            didPlayerOne = GameResults.TIE;

        switch(didPlayerOne) {
            case WIN:
                io.println("Player 1 wins!");
                break;
            case LOSS:
                io.println("Player 2 wins!");
                break;
            case TIE:
                io.println("A tie!");
                break;
        }
    }

    /**
     * Display the final scores to the Players
     * @param io
     * @param scores - both Player's scores
     */
    public void displayFinalScores(IO io, int[] scores) {
        io.println("\tplayer 1:" + scores[0]);
        io.println("\tplayer 2:" + scores[1]);
    }
}

