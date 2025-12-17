package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

public class SubFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 3;
    }

}
