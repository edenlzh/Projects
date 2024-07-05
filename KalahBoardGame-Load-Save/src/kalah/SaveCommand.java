package kalah;

import com.qualitascorpus.testsupport.IO;
/**
 * Concrete Command for the Command design pattern
 *
 * This Command objectifies the Save Command call and can execute the command
 */
public class SaveCommand implements Command{
    private GameHandler _gamehandler;
    private IO _io;

    public SaveCommand(GameHandler gameHandler, IO io){
        _gamehandler = gameHandler;
        _io = io;
    }
    @Override
    public void execute() {
        _gamehandler.saveGame(_io);
    }
}
