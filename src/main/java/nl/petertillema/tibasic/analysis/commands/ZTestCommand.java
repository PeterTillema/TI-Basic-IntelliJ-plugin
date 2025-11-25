package nl.petertillema.tibasic.analysis.commands;

public class ZTestCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 2;
    }

    @Override
    public int getMaxNrArguments() {
        return 7;
    }

}
