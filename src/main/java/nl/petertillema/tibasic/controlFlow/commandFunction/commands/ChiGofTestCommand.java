package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class ChiGofTestCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 5;
    }

}
