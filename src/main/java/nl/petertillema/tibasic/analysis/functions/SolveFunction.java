package nl.petertillema.tibasic.analysis.functions;

public class SolveFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 3;
    }

    @Override
    public int getMaxNrArguments() {
        return 4;
    }

}
