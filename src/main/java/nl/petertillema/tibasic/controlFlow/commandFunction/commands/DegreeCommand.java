package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.BooleanDescriptor;
import nl.petertillema.tibasic.controlFlow.type.DfTypes;
import org.jetbrains.annotations.NotNull;

public class DegreeCommand implements TIBasicCommand {

    @Override
    public int getMinNrArguments() {
        return 0;
    }

    @Override
    public int getMaxNrArguments() {
        return 0;
    }

    @Override
    public void evalCommand(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, DfaValue... arguments) {
        DfaVariableValue radianVar = factory.getVarFactory().createVariableValue(BooleanDescriptor.RADIAN_VARIABLE);
        state.setVarValue(radianVar, factory.fromDfType(DfTypes.FALSE));
    }

}
