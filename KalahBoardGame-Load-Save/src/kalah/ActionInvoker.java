package kalah;

/**
 * Invoker class for Command design pattern
 *
 * @setCommand get the action command to be invoked
 * @executeCommand execute the command
 */
public class ActionInvoker {
    Command _command;

    public void setCommand(Command command){
        _command = command;
    }
    public void executeCommand(){
        _command.execute();
    }
}
