package kalah;

import java.util.ArrayList;
import java.util.List;
/**
 * Caretaker class for the Memento design pattern
 *
 * Saves List of recently saved Board's but only most recently saved one is accessible
 */
public class SavedBoard {
    private List<GameBoard> _savedBoardList;

    /**
     * Default Constructor
     */
    SavedBoard() {
        _savedBoardList = new ArrayList<>();
    }

    /**
     * Adds a new saved Board
     * @param newBoardSaveState - new Board to be saved
     */
    public void addSavedBoardState(GameBoard newBoardSaveState) {
        _savedBoardList.add(newBoardSaveState);
    }

    /**
     * @returns the most recently saved Board in List
     */
    public GameBoard getRecentlySavedBoard() {
        if(_savedBoardList.size() != 0)
            return _savedBoardList.get(_savedBoardList.size() -1);
        else
            return null;
    }
}
