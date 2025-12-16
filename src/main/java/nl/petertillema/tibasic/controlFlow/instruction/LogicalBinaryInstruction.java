package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.Synthetic;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfElementMap;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.LogicalOperator;
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

        if (leftType instanceof DfListType && rightType instanceof DfListType) {
            DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) stateBefore, (DfaVariableValue) dfaLeft);
            DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) stateBefore, (DfaVariableValue) dfaRight);
            DfaValueFactory factory = interpreter.getFactory();
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            leftMap.execBiOperator(rightMap, (leftValue, rightValue) -> {
                Truth l = Truth.fromDfType(leftValue);
                Truth r = Truth.fromDfType(rightValue);
                return l.applyOperator(r, operator);
            }).exportTo((DfaMemoryStateImpl) stateBefore, out);
            pushResult(interpreter, stateBefore, out);
            return nextStates(interpreter, stateBefore);
        }
        if (leftType instanceof DfListType && rightType instanceof DfBigDecimalType rightNum) {
            DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) stateBefore, (DfaVariableValue) dfaLeft);
            Truth r = Truth.fromDfType(rightNum);
            DfaValueFactory factory = interpreter.getFactory();
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            leftMap.execOperator(v -> {
                Truth l = Truth.fromDfType(v);
                return l.applyOperator(r, operator);
            }).exportTo((DfaMemoryStateImpl) stateBefore, out);
            pushResult(interpreter, stateBefore, out);
            return nextStates(interpreter, stateBefore);
        }
        if (leftType instanceof DfBigDecimalType leftNum && rightType instanceof DfListType) {
            DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) stateBefore, (DfaVariableValue) dfaRight);
            Truth l = Truth.fromDfType(leftNum);
            DfaValueFactory factory = interpreter.getFactory();
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            rightMap.execOperator(v -> {
                Truth r = Truth.fromDfType(v);
                return l.applyOperator(r, operator);
            }).exportTo((DfaMemoryStateImpl) stateBefore, out);
            pushResult(interpreter, stateBefore, out);
            return nextStates(interpreter, stateBefore);
        }

        Truth l = Truth.fromDfType(leftType);
        Truth r = Truth.fromDfType(rightType);

        DfType result = l.applyOperator(r, operator);
        if (result instanceof DfBigDecimalConstantType) {
            pushResult(interpreter, stateBefore, result);
            return nextStates(interpreter, stateBefore);
        }
        return splitUnknown(interpreter, stateBefore);
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

        static Truth fromDfType(DfType type) {
            if (type instanceof DfBigDecimalType bd) {
                BigDecimalRangeSet range = bd.range();
                boolean canBeZero = range.contains(ZERO);
                boolean canBeNonZero = !range.subtract(point(ZERO)).isEmpty();
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
