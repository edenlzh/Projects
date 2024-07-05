package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * Concrete Command for the Command design pattern
 *
 * This Command objectifies the Load Command call and can execute the command
 */
public class LoadCommand implements Command{
    private GameHandler _gamehandler;
    private IO _io;

    public LoadCommand(GameHandler gameHandler, IO io){
        _gamehandler = gameHandler;
        _io = io;
    }
    @Override
    public void execute() {
        _gamehandler.loadGame(_io);
    }
}
