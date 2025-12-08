package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.LogicalOperator;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.point;

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

        Truth l = Truth.fromDfType(leftType);
        Truth r = Truth.fromDfType(rightType);

        // Try to derive a precise result; otherwise split the flow and push both 0 and 1
        switch (operator) {
            case AND -> {
                if (l.isAlwaysFalse() || r.isAlwaysFalse()) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ZERO));
                    return nextStates(interpreter, stateBefore);
                }
                if (l.isAlwaysTrue() && r.isAlwaysTrue()) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ONE));
                    return nextStates(interpreter, stateBefore);
                }
                return splitUnknown(interpreter, stateBefore);
            }
            case OR -> {
                if (l.isAlwaysTrue() || r.isAlwaysTrue()) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ONE));
                    return nextStates(interpreter, stateBefore);
                }
                if (l.isAlwaysFalse() && r.isAlwaysFalse()) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ZERO));
                    return nextStates(interpreter, stateBefore);
                }
                return splitUnknown(interpreter, stateBefore);
            }
            case XOR -> {
                if ((l.isAlwaysFalse() && r.isAlwaysFalse()) || (l.isAlwaysTrue() && r.isAlwaysTrue())) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ZERO));
                    return nextStates(interpreter, stateBefore);
                }
                if ((l.isAlwaysTrue() && r.isAlwaysFalse()) || (l.isAlwaysFalse() && r.isAlwaysTrue())) {
                    pushResult(interpreter, stateBefore, fromValue(BigDecimal.ONE));
                    return nextStates(interpreter, stateBefore);
                }
                return splitUnknown(interpreter, stateBefore);
            }
        }
        return splitUnknown(interpreter, stateBefore);
    }

    private DfaInstructionState[] splitUnknown(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        ArrayList<DfaInstructionState> states = new ArrayList<>(2);
        DfaMemoryState zeroState = stateBefore.createCopy();
        pushResult(interpreter, zeroState, fromValue(BigDecimal.ZERO));
        states.add(nextState(interpreter, zeroState));
        pushResult(interpreter, stateBefore, fromValue(BigDecimal.ONE));
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

        static Truth fromDfType(DfType type) {
            if (type instanceof DfBigDecimalType bd) {
                BigDecimalRangeSet range = bd.range();
                boolean canBeZero = range.contains(BigDecimal.ZERO);
                boolean canBeNonZero = !range.subtract(point(BigDecimal.ZERO)).isEmpty();
                if (canBeZero && !canBeNonZero) return ALWAYS_FALSE;
                if (!canBeZero && canBeNonZero) return ALWAYS_TRUE;
                return MAYBE;
            }
            return MAYBE;
        }
    }

    @Override
    public String toString() {
        return "LOGICAL_OP " + operator;
    }
}
