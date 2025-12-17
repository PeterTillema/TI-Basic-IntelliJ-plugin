package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class ArchiveCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return Integer.MAX_VALUE;
    }

}
