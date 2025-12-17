package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

public class RandIntNoRepFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 2;
    }

    @Override
    public int getMaxNrArguments() {
        return 3;
    }

}
