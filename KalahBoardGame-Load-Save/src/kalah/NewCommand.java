package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * Concrete Command for the Command design pattern
 *
 * This Command objectifies the New Command call and can execute the command
 */
public class NewCommand implements Command{
    private GameHandler _gameHandler;
    private IO _io;

    public NewCommand(GameHandler gameHandler, IO io){
        _gameHandler = gameHandler;
        _io = io;
    }

    @Override
    public void execute() {
        _gameHandler.newGame(_io);
    }
}
