package kalah;

import java.util.ArrayList;
/**
 * Player class that holds each individual players part of the playing board.
 * It handles the creation of their Houses and Store on the board and handles
 * their score.
 */
public class Player {
    private final int NUMHOUSES = 6;
    private ArrayList<Pit> _playerPits;
    private int _score;

    //Default Constructor, initialises the Player state
    Player() {
        initialisePlayerPits();
        _score = 0;
    }

    //Initialises the ArrayList of Pits for that Player with default # seeds
    private void initialisePlayerPits() {
        _playerPits = new ArrayList<>();
        for(int i = 0; i < NUMHOUSES; i++)
            _playerPits.add(new House());
        _playerPits.add(new Store());
    }

    public ArrayList<Pit> getPlayerPits() { return _playerPits; }

    //Calculates the Players score
    public void calcScore() {
        for(int i = 0; i < _playerPits.size(); i++)
            _score += _playerPits.get(i).getNumSeed();
    }

    //Returns the Player's score
    public int getScore() { return _score; }

    //Returns # Houses which the player has
    public int getNUMHOUSES() { return NUMHOUSES; }
}
