package nl.petertillema.tibasic.analysis.commands;

import com.intellij.codeInspection.dataFlow.value.DfaValue;
import org.jetbrains.annotations.NotNull;

public interface TIBasicCommand {

    int getMinNrArguments();

    int getMaxNrArguments();

    default void evalCommand(@NotNull DfaValue @NotNull ... arguments) {
    }

}
