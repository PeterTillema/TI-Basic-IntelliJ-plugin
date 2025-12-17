package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import org.jetbrains.annotations.NotNull;

public interface TIBasicFunction {

    int getMinNrArguments();

    int getMaxNrArguments();

    default DfType evalFunction(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        return DfType.TOP;
    }

}
