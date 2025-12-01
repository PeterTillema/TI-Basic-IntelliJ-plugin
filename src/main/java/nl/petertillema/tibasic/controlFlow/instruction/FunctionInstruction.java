package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.analysis.functions.TIBasicFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.analysis.TIBasicCommandFunctionMap.FUNCTION_MAP;

public class FunctionInstruction extends EvalInstruction {

    private final String functionName;

    public FunctionInstruction(String functionName, int operands, @Nullable DfaAnchor anchor) {
        super(anchor, operands);
        this.functionName = functionName;
    }

    @Override
    public @NotNull DfaValue eval(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        TIBasicFunction functionImpl = FUNCTION_MAP.get(functionName);
        if (functionImpl != null) {
            DfType outDfType = functionImpl.evalFunction(arguments);
            return factory.fromDfType(outDfType);
        } else {
            System.out.println("Unknown function!!! - " + functionName);
            return factory.getUnknown();
        }
    }

    @Override
    public String toString() {
        return "FUNCTION " + functionName;
    }
}
