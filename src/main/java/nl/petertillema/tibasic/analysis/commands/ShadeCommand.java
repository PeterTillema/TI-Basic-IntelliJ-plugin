package nl.petertillema.tibasic.analysis.commands;

public class ShadeCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 2;
    }

    @Override
    public int getMaxNrArguments() {
        return 7;
    }

}
