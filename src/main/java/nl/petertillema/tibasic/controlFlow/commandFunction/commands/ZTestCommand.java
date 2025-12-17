package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

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
