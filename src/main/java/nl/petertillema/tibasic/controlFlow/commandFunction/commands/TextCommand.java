package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class TextCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return Integer.MAX_VALUE;
    }

}
