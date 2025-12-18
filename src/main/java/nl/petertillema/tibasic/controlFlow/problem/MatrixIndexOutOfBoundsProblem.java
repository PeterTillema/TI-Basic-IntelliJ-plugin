package nl.petertillema.tibasic.controlFlow.problem;

import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;

public class MatrixIndexOutOfBoundsProblem extends IndexOutOfBoundsProblem {

    @Override
    protected SpecialFieldDescriptor getSpecialFieldDescriptor() {
        return SpecialFieldDescriptor.MATRIX_LENGTH;
    }

}
