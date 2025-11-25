package nl.petertillema.tibasic.analysis.commands;

public class ShadeTCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 4;
    }

}
