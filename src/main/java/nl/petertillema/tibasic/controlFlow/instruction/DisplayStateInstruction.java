package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import org.jetbrains.annotations.NotNull;

/**
 * Temporary instruction for debugging purposed. Prints the state after the entire flow is run.
 */
public class DisplayStateInstruction extends Instruction {

    public DisplayStateInstruction() {
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        System.out.println(stateBefore);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "DISP_STATE";
    }
}
