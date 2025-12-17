package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

public class FMinFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 4;
    }

    @Override
    public int getMaxNrArguments() {
        return 5;
    }

}
