package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class FnOffCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 0;
    }

    @Override
    public int getMaxNrArguments() {
        return Integer.MAX_VALUE;
    }

}
