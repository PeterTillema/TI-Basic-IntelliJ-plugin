package nl.petertillema.tibasic.analysis.functions;

public class NDerivFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 4;
    }

}
