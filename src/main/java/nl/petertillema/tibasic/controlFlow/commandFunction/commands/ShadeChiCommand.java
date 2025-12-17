package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

public class ShadeChiCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 4;
    }

}
