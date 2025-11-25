package nl.petertillema.tibasic.analysis.commands;

public class SortDCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return Integer.MAX_VALUE;
    }

}
