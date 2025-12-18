package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.lang.ir.Instruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaControlTransferValue;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.ListElementDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import nl.petertillema.tibasic.controlFlow.problem.IndexOutOfBoundsProblem;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Instruction which fetches an element with a given index from a list. The length of the list must be known before
 * executing this instruction, otherwise the result will be an unknown value. If multiple integer indexes are candidate,
 * the result will be joined from all found values.
 */
public class GetListElementInstruction extends ExpressionPushingInstruction {

    private final @NotNull IndexOutOfBoundsProblem problem;
    private final @Nullable DfaControlTransferValue outOfBoundsTransfer;

    public GetListElementInstruction(@Nullable DfaAnchor anchor,
                                     @NotNull IndexOutOfBoundsProblem indexProblem,
                                     @Nullable DfaControlTransferValue outOfBoundsTransfer) {
        super(anchor);
        this.problem = indexProblem;
        this.outOfBoundsTransfer = outOfBoundsTransfer;
    }

    @Override
    public @NotNull Instruction bindToFactory(@NotNull DfaValueFactory factory) {
        DfaControlTransferValue newTransfer = outOfBoundsTransfer == null ? null : outOfBoundsTransfer.bindToFactory(factory);
        return new GetListElementInstruction(getDfaAnchor(), problem, newTransfer);
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        DfaValue index = stateBefore.pop();
        DfaValue list = stateBefore.pop();
        List<DfaInstructionState> finalStates = new ArrayList<>();
        if (outOfBoundsTransfer != null) {
            finalStates.addAll(IndexOutOfBoundsProblem.dispatchTransfer(interpreter, stateBefore.createCopy(), outOfBoundsTransfer));
        }
        DfaInstructionState[] states = problem.processOutOfBounds(interpreter, stateBefore, index, list, outOfBoundsTransfer);
        if (states != null) return states;
        BigDecimalRangeSet rangeSet = DfBigDecimalType.extractRange(stateBefore.getDfType(index));
        DfaValue result = ListElementDescriptor.getListElementValue(interpreter.getFactory(), stateBefore, list, rangeSet);
        pushResult(interpreter, stateBefore, result);
        finalStates.add(nextState(interpreter, stateBefore));
        return finalStates.toArray(DfaInstructionState.EMPTY_ARRAY);
    }

    @Override
    public List<VariableDescriptor> getRequiredDescriptors(@NotNull DfaValueFactory factory) {
        return List.of(SpecialFieldDescriptor.LIST_LENGTH);
    }

    @Override
    public String toString() {
        return "GET_LIST_ELEMENT";
    }

}
