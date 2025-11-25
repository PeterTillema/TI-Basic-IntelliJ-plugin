package nl.petertillema.tibasic.analysis.functions;

public class ToStringFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 2;
    }

}
