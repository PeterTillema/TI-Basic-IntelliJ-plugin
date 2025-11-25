package nl.petertillema.tibasic.analysis.commands;

public class SetTmFmtCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 1;
    }

}
