package nl.petertillema.tibasic.controlFlow.commandFunction.commands;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import org.jetbrains.annotations.NotNull;

public interface TIBasicCommand {

    int getMinNrArguments();

    int getMaxNrArguments();

    default void evalCommand(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, DfaValue ... arguments) {
    }

}
