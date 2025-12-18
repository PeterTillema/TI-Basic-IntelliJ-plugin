package nl.petertillema.tibasic.controlFlow.problem;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.UnsatisfiedConditionProblem;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaCondition;
import com.intellij.codeInspection.dataFlow.value.DfaControlTransferValue;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import com.intellij.util.ThreeState;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

public abstract class IndexOutOfBoundsProblem implements UnsatisfiedConditionProblem {

    protected abstract SpecialFieldDescriptor getSpecialFieldDescriptor();

    private boolean applyBoundsCheck(@NotNull DfaMemoryState memState,
                                     @NotNull DfaValue array,
                                     @NotNull DfaValue index) {
        DfaValueFactory factory = index.getFactory();
        DfaValue length = getSpecialFieldDescriptor().createValue(factory, array);
        DfaCondition lengthMoreThanZero = length.cond(RelationType.GT, fromValue(0));
        if (!memState.applyCondition(lengthMoreThanZero)) return false;
        DfaCondition indexPositive = index.cond(RelationType.GE, fromValue(1));
        if (!memState.applyCondition(indexPositive)) return false;
        DfaCondition indexLessEqualLength = index.cond(RelationType.LE, length);
        return memState.applyCondition(indexLessEqualLength);
    }

    public DfaInstructionState @Nullable [] processOutOfBounds(@NotNull DataFlowInterpreter interpreter,
                                                                @NotNull DfaMemoryState stateBefore,
                                                                @NotNull DfaValue index,
                                                                @NotNull DfaValue array,
                                                                @Nullable DfaControlTransferValue outOfBoundsTransfer) {
        boolean alwaysOutOfBounds = !applyBoundsCheck(stateBefore, array, index);
        ThreeState failed = alwaysOutOfBounds ? ThreeState.YES : ThreeState.UNSURE;
        interpreter.getListener().onCondition(this, index, failed, stateBefore);
        if (alwaysOutOfBounds) {
            if (outOfBoundsTransfer != null) {
                List<DfaInstructionState> states = dispatchTransfer(interpreter, stateBefore, outOfBoundsTransfer);
                return states.toArray(DfaInstructionState.EMPTY_ARRAY);
            }
            return DfaInstructionState.EMPTY_ARRAY;
        }
        return null;
    }

    public static @NotNull List<DfaInstructionState> dispatchTransfer(@NotNull DataFlowInterpreter interpreter,
                                                               @NotNull DfaMemoryState stateBefore,
                                                               @NotNull DfaControlTransferValue outOfBoundsTransfer) {
        List<DfaInstructionState> states = outOfBoundsTransfer.dispatch(stateBefore, interpreter);
        for (DfaInstructionState state : states) {
            state.getMemoryState().markEphemeral();
        }
        return states;
    }

}
