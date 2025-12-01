package nl.petertillema.tibasic.analysis.functions;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;

public interface TIBasicFunction {

    int getMinNrArguments();

    int getMaxNrArguments();

    default DfType evalFunction(DfaMemoryState state, DfaValue ... arguments) {
        return DfType.TOP;
    }

}
