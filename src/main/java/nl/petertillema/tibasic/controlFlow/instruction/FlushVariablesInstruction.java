package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FlushVariablesInstruction extends Instruction {

    private final Predicate<DfaVariableValue> predicate;

    public FlushVariablesInstruction(Predicate<DfaVariableValue> predicate) {
        this.predicate = predicate;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        stateBefore.flushVariables(predicate);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "FLUSH_VARS";
    }
}
