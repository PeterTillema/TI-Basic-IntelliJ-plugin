package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaCondition;
import com.intellij.codeInspection.dataFlow.value.DfaTypeValue;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.TypeEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;

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

        DfType leftType = stateBefore.getDfType(dfaLeft);
        DfType rightType = stateBefore.getDfType(dfaRight);

        DfaValue out = TypeEvaluator.evaluateBinaryOperator(interpreter.getFactory(), stateBefore, dfaLeft, dfaRight, (v1, v2) -> {
            DfaCondition.Exact value = DfaCondition.tryEvaluate(v1, relation, v2);
            if (value != null) return fromValue(value == DfaCondition.getTrue() ? 1 : 0);
            return fromRange(pointSet(BigDecimal.ZERO, BigDecimal.ONE));
        });
        // If the exact result between two numbers couldn't be determined, create two states with both outcomes rather
        // than having a pointSet with 0 and 1.
        if ((leftType instanceof DfBigDecimalType && rightType instanceof DfBigDecimalType) &&
                (!(out instanceof DfaTypeValue typeValue) || !(typeValue.getDfType() instanceof DfBigDecimalConstantType))) {
            return splitUnknown(interpreter, stateBefore);
        }
        pushResult(interpreter, stateBefore, out);
        return nextStates(interpreter, stateBefore);
    }

    private DfaInstructionState[] splitUnknown(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        ArrayList<DfaInstructionState> states = new ArrayList<>(2);
        DfaMemoryState zeroState = stateBefore.createCopy();
        pushResult(interpreter, zeroState, fromValue(0));
        states.add(nextState(interpreter, zeroState));
        pushResult(interpreter, stateBefore, fromValue(1));
        states.add(nextState(interpreter, stateBefore));
        return states.toArray(DfaInstructionState.EMPTY_ARRAY);
    }

    @Override
    public String toString() {
        return "BINARY_OP " + relation.toString();
    }
}
