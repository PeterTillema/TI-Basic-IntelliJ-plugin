package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaTypeValue;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import nl.petertillema.tibasic.controlFlow.operator.LogicalOperator;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.TypeEvaluator;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.point;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;

public class LogicalBinaryInstruction extends ExpressionPushingInstruction {

    private final LogicalOperator operator;

    public LogicalBinaryInstruction(@Nullable DfaAnchor anchor, @NotNull LogicalOperator operator) {
        super(anchor);
        this.operator = operator;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        DfaValue dfaRight = stateBefore.pop();
        DfaValue dfaLeft = stateBefore.pop();

        DfType leftType = stateBefore.getDfType(dfaLeft);
        DfType rightType = stateBefore.getDfType(dfaRight);

        DfaValue out = TypeEvaluator.evaluateBinaryOperator(interpreter.getFactory(), stateBefore, dfaLeft, dfaRight, (leftValue, rightValue) -> {
            Truth l = Truth.fromDfType(leftValue);
            Truth r = Truth.fromDfType(rightValue);
            return l.applyOperator(r, operator);
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

    private enum Truth {
        ALWAYS_FALSE, ALWAYS_TRUE, MAYBE;

        boolean isAlwaysFalse() {
            return this == ALWAYS_FALSE;
        }

        boolean isAlwaysTrue() {
            return this == ALWAYS_TRUE;
        }

        public DfType applyOperator(Truth other, LogicalOperator operator) {
            DfType unknown = fromRange(pointSet(ZERO, ONE));
            return switch (operator) {
                case AND ->
                        (isAlwaysFalse() || other.isAlwaysFalse()) ? fromValue(0) : (isAlwaysTrue() && other.isAlwaysTrue()) ? fromValue(1) : unknown;
                case OR ->
                        (isAlwaysTrue() || other.isAlwaysTrue()) ? fromValue(1) : (isAlwaysFalse() && other.isAlwaysFalse()) ? fromValue(0) : unknown;
                case XOR ->
                        ((isAlwaysFalse() && other.isAlwaysFalse()) || (isAlwaysTrue() && other.isAlwaysTrue())) ? fromValue(0) : ((isAlwaysTrue() && other.isAlwaysFalse()) || (isAlwaysFalse() && other.isAlwaysTrue())) ? fromValue(1) : unknown;
            };
        }

        static Truth fromDfType(DfBigDecimalType bd) {
            BigDecimalRangeSet range = bd.range();
            boolean canBeZero = range.contains(ZERO);
            boolean canBeNonZero = !range.subtract(point(ZERO)).isEmpty();
            if (canBeZero && !canBeNonZero) return ALWAYS_FALSE;
            if (!canBeZero && canBeNonZero) return ALWAYS_TRUE;
            return MAYBE;
        }
    }

    @Override
    public String toString() {
        return "LOGICAL_OP " + operator;
    }
}
