package nl.petertillema.tibasic.analysis.functions;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import org.jetbrains.annotations.NotNull;

public interface TIBasicFunction {

    int getMinNrArguments();

    int getMaxNrArguments();

    default DfType evalFunction(@NotNull DfaValue @NotNull ... arguments) {
        return DfType.TOP;
    }

}
