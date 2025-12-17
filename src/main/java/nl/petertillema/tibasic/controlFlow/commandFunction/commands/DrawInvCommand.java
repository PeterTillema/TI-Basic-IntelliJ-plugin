package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class DrawInvCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 2;
    }

}
