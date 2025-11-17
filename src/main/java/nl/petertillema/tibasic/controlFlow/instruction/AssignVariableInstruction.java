package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Assignment instruction: assigns the provided value to the target variable in the DFA memory state
 * and returns the assigned value.
 */
public class AssignVariableInstruction extends ExpressionPushingInstruction {

    private final @NotNull DfaVariableValue target;

    public AssignVariableInstruction(@Nullable DfaAnchor anchor, @NotNull DfaVariableValue target) {
        super(anchor);
        this.target = target;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        var value = stateBefore.pop();
        interpreter.getListener().beforeAssignment(value, target, stateBefore, getDfaAnchor());
        stateBefore.setVarValue(target, value);
        interpreter.getListener().afterAssignment(value, target, stateBefore, getDfaAnchor());
        pushResult(interpreter, stateBefore, value, value);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "ASSIGN " + target;
    }
}
