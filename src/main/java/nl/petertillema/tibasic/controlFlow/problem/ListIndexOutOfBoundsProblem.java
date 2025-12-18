package nl.petertillema.tibasic.controlFlow.problem;

import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;

public class ListIndexOutOfBoundsProblem extends IndexOutOfBoundsProblem {

    @Override
    protected SpecialFieldDescriptor getSpecialFieldDescriptor() {
        return SpecialFieldDescriptor.LIST_LENGTH;
    }

}
