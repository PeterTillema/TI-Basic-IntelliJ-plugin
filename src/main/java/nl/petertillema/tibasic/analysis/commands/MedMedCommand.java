package nl.petertillema.tibasic.analysis.commands;

public class MedMedCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 0;
    }

    @Override
    public int getMaxNrArguments() {
        return 4;
    }

}
