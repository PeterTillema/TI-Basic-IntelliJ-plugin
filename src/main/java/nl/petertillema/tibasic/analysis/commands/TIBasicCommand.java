package nl.petertillema.tibasic.analysis.commands;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;

public interface TIBasicCommand {

    int getMinNrArguments();

    int getMaxNrArguments();

    default void evalCommand(DfaMemoryState state, DfaValue ... arguments) {
    }

}
