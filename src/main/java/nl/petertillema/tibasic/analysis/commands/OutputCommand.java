package nl.petertillema.tibasic.analysis.commands;

public class OutputCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 3;
    }

}
