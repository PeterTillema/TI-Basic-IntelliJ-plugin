package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import org.jetbrains.annotations.NotNull;

public class FlushAllVariablesInstruction extends Instruction {

    public FlushAllVariablesInstruction() {
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        stateBefore.flushVariables(v -> true);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "FLUSH_ALL";
    }
}
