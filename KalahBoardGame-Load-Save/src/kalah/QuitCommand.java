package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * Concrete Command for the Command design pattern
 *
 * This Command objectifies the Quit Command call and can execute the command
 */
public class QuitCommand implements Command{
    private GameHandler _gamehandler;
    private IO _io;

    public QuitCommand(GameHandler gameHandler, IO io){
        _gamehandler = gameHandler;
        _io = io;
    }
    @Override
    public void execute() {
        _gamehandler.userHasQuit(_io);
    }
}
