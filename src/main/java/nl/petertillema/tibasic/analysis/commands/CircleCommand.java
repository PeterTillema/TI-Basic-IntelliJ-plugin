package nl.petertillema.tibasic.analysis.commands;

public class CircleCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 5;
    }

}
