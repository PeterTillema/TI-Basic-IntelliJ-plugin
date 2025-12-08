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
            ((DfaMemoryStateImpl) stateBefore).recordVariableType(destinationVar, stateBefore.getDfType(value));
            if (value instanceof DfaVariableValue var) {
                var.getDependentVariables().forEach(depVar ->
                        stateBefore.setVarValue(depVar.withQualifier(destinationVar), depVar));
            }

            interpreter.getListener().afterAssignment(value, destination, stateBefore, getDfaAnchor());
        }

        pushResult(interpreter, stateBefore, value);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "ASSIGN";
    }

}
