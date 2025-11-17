package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReadVariableInstruction extends ExpressionPushingInstruction {

    private final @NotNull DfaVariableValue target;

    public ReadVariableInstruction(@Nullable DfaAnchor anchor, @NotNull DfaVariableValue target) {
        super(anchor);
        this.target = target;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        var dfType = stateBefore.getDfType(target);
        pushResult(interpreter, stateBefore, interpreter.getFactory().fromDfType(dfType));
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "READ " + target;
    }

}
