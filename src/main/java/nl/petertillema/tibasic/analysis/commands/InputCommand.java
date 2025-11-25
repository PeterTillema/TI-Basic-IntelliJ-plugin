package nl.petertillema.tibasic.analysis.commands;

public class InputCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 2;
    }

}
