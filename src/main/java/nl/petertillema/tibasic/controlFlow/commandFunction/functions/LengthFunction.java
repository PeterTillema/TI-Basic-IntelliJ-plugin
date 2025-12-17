package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import org.jetbrains.annotations.NotNull;

public class LengthFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 1;
    }

    @Override
    public DfaValue evalFunction(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        DfType varType = state.getDfType(arguments[0]);
        if (varType instanceof DfMatrixType || !(varType instanceof DfListType listType)) return factory.getUnknown();
        return factory.fromDfType(listType.getSpecialFieldType());
    }
}
