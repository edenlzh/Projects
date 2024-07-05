package kalah;
/**
 * Abstract class that describes the Pit functionality (can be a
 * House or a Store). Each Pit has some number of seeds which can be
 * manipulated.
 */
public abstract class Pit {
    protected int _numSeed;

    //Adds specified # of seeds to the Pit
    protected void addSeed(int seeds) {_numSeed += seeds; }

    //Retrieves # seeds in Pit
    protected int getNumSeed() { return _numSeed; }

    //Empties the Pit (i.e. at start of turn)
    protected void emptyPit() { _numSeed = 0; }

    //Loads the Pit when a copy of the Board is needed
    protected void loadPit(int loadSeeds) { _numSeed = loadSeeds; }

    //Returns # seeds captured and empties the pit
    protected int captureSeed() {
        int seeds = _numSeed;
        emptyPit();
        return seeds;
    }
}
