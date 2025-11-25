package nl.petertillema.tibasic.analysis.commands;

public class LineCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 4;
    }

    @Override
    public int getMaxNrArguments() {
        return 7;
    }

}
