package nl.petertillema.tibasic.analysis.functions;

public class BinompdfFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 2;
    }

    @Override
    public int getMaxNrArguments() {
        return 3;
    }

}
