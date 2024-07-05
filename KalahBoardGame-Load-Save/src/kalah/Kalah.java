package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * Client Class for the Command design pattern
 *
 * This class is the starting point for a Kalah
 * implementation using the test infrastructure.
 */
public class Kalah {
	GameHandler gameHandler = new GameHandler();

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}

	/**
	 * Plays the game
	 * @param io
	 */
	public void play(IO io) {
		gameHandler.initialiseNewGameState();
		gameHandler.getBoardDisplay(io);
		do { //while game not over, keep making turns
			if(gameHandler.isGameOver())
				gameHandler.endGame(io);
			int selectedHouse = gameHandler.getPlayerTurnInput(io);

			//valid move
			if(!(selectedHouse == -1))
				gameHandler.playTurn(selectedHouse, io);
		} while (gameHandler.isPlaying());
		if(!gameHandler.hasUserQuit() || gameHandler.isGameOver())
			gameHandler.endGame(io);
	}
	public void play(IO io, boolean vertical, boolean bmf) {
		// DO NOT CHANGE. Only here for backwards compatibility
	        play(io);
	}
}
