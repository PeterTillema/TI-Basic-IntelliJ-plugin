package nl.petertillema.tibasic.analysis.commands;

public class FnOffCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 0;
    }

    @Override
    public int getMaxNrArguments() {
        return Integer.MAX_VALUE;
    }

}
