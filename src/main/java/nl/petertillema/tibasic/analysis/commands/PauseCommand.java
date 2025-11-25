package nl.petertillema.tibasic.analysis.commands;

public class PauseCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 0;
    }

    @Override
    public int getMaxNrArguments() {
        return 1;
    }

}
