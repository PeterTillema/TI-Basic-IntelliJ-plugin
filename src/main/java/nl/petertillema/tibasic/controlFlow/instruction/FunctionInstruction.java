package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.controlFlow.commandFunction.functions.TIBasicFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.controlFlow.commandFunction.TIBasicCommandFunctionMap.FUNCTION_MAP;

/**
 * Executes a built-in function with the given arguments. The minimum number of arguments is checked against, it's up to
 * the individual functions to check the correct number of arguments. The result of the function is pushed to the stack.
 */
public class FunctionInstruction extends EvalInstruction {

    private final String functionName;

    public FunctionInstruction(String functionName, int operands, @Nullable DfaAnchor anchor) {
        super(anchor, operands);
        this.functionName = functionName;
    }

    @Override
    public @NotNull DfaValue eval(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        TIBasicFunction functionImpl = FUNCTION_MAP.get(functionName);
        if (functionImpl != null && arguments.length >= functionImpl.getMinNrArguments()) {
            return functionImpl.evalFunction(factory, state, arguments);
        } else {
            return factory.getUnknown();
        }
    }

    @Override
    public String toString() {
        return "FUNCTION " + functionName;
    }
}
