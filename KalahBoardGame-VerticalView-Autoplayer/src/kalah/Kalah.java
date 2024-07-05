package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure. Remove this comment (or rather, replace it
 * with something more appropriate)
 */

public class Kalah {
	public static void main(String[] args) {
		new Kalah().play(new MockIO(), false, true);
	}
	/**
	 * Play a game of Kalah.
	 * @param io There to get input from and direct output to
	 * @param vertical If true, then play the game between two humans but orient the board vertically.
	 * If it is false <b>and</b> bmf is false then orient the board horizontally.
	 * @param bmf If vertical is false <b>and</b> this is true then play the game where the second player
	 * (P2) is the "best first move" robot.
	 */
	public void play(IO io, boolean vertical, boolean bmf) {
		GameRound round12 = new GameRound();
		GameRound round1R = new GameRound();
		if (vertical) {
			// p1, p2, board vertical
			round12.P1P2round(io, vertical);
		}
		else if (!vertical && (!bmf)) {
			// p1, p2, board horizontal
			round12.P1P2round(io, vertical);
		}
		else if ((!vertical) && (bmf)) {
			// p1, robot, board horizontal
			round1R.P1RobotRound(io, vertical);
		}
	}
}
