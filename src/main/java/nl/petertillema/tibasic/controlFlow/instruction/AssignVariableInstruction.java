package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssignVariableInstruction extends ExpressionPushingInstruction {

    public AssignVariableInstruction(@Nullable DfaAnchor anchor) {
        super(anchor);
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter,
                                        @NotNull DfaMemoryState stateBefore) {
        DfaValue destination = stateBefore.pop();
        DfaValue value = stateBefore.pop();

        if (destination instanceof DfaVariableValue destinationVar) {
            interpreter.getListener().beforeAssignment(value, destination, stateBefore, getDfaAnchor());

            stateBefore.flushVariable(destinationVar);
            copy(stateBefore, value, destinationVar);

            interpreter.getListener().afterAssignment(value, destination, stateBefore, getDfaAnchor());
            pushResult(interpreter, stateBefore, destinationVar);
        } else {
            pushResult(interpreter, stateBefore, value);
        }

        return nextStates(interpreter, stateBefore);
    }

    private static void copy(@NotNull DfaMemoryState state,
                             @NotNull DfaValue source,
                             @NotNull DfaVariableValue destination) {
        if (!(source instanceof DfaVariableValue sourceVar) || sourceVar.getDependentVariables().isEmpty()) {
            // A simple variable could be copied directly, such that it keeps the equivalence between the source
            // and the destination. The advantage is that within a loop, if a state is removed, the state is removed
            // from the source as well. So, if we have a Repeat B / getKey->B / End, then after the loop both B and Ans
            // are non-zero. If we always use recordVariableType, the state is not removed from Ans which is then either
            // 0 (should be false) or one of the other non-zero values.
            state.setVarValue(destination, source);
        } else {
            ((DfaMemoryStateImpl) state).recordVariableType(destination, state.getDfType(source));
            for (DfaVariableValue child : sourceVar.getDependentVariables()) {
                DfaVariableValue mappedChild = child.withQualifier(destination);
                copy(state, child, mappedChild);
            }
        }
    }

    @Override
    public String toString() {
        return "ASSIGN";
    }

}
