package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.value.DfaCondition;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

public class BooleanBinaryInstruction extends ExpressionPushingInstruction {

    private final RelationType relation;

    public BooleanBinaryInstruction(@Nullable DfaAnchor anchor, @NotNull RelationType relation) {
        super(anchor);
        this.relation = relation;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        DfaValue dfaRight = stateBefore.pop();
        DfaValue dfaLeft = stateBefore.pop();

        DfaCondition condition = dfaLeft.cond(relation, dfaRight);
        if (condition == DfaCondition.getFalse()) {
            pushResult(interpreter, stateBefore, fromValue(0));
            return nextStates(interpreter, stateBefore);
        }
        if (condition == DfaCondition.getTrue()) {
            pushResult(interpreter, stateBefore, fromValue(1));
            return nextStates(interpreter, stateBefore);
        }

        var copy = stateBefore.createCopy();
        if (copy.applyCondition(condition)) {
            ArrayList<DfaInstructionState> states = new ArrayList<>(2);
            DfaMemoryState zeroState = stateBefore.createCopy();
            pushResult(interpreter, zeroState, fromValue(0));
            states.add(nextState(interpreter, zeroState));
            pushResult(interpreter, stateBefore, fromValue(1));
            states.add(nextState(interpreter, stateBefore));
            return states.toArray(DfaInstructionState.EMPTY_ARRAY);
        }

        pushResult(interpreter, stateBefore, fromValue(0));
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "BINARY_OP " + relation.toString();
    }
}
