package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.ir.ControlFlow;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple instruction with splits the state into multiple other instructions, based on the given offsets. Its sole
 * purpose is for Menu( statements.
 */
public class MultipleGotoInstruction extends Instruction {

    private final List<ControlFlow.ControlFlowOffset> offsets;

    public MultipleGotoInstruction(List<ControlFlow.ControlFlowOffset> offsets) {
        this.offsets = offsets;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        ArrayList<DfaInstructionState> result = new ArrayList<>(offsets.size());

        for (ControlFlow.ControlFlowOffset offset : offsets) {
            DfaMemoryState stateCopy = stateBefore.createCopy();
            result.add(new DfaInstructionState(interpreter.getInstruction(offset.getInstructionOffset()), stateCopy));
        }

        return result.toArray(DfaInstructionState.EMPTY_ARRAY);
    }

    @Override
    public int @NotNull [] getSuccessorIndexes() {
        return offsets.stream()
                .mapToInt(ControlFlow.ControlFlowOffset::getInstructionOffset)
                .toArray();
    }
}
