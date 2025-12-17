package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.interpreter.DataFlowInterpreter;
import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.DfaInstructionState;
import com.intellij.codeInspection.dataFlow.lang.ir.ExpressionPushingInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaCondition;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.TypeEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;

/**
 * An instruction which performs a boolean binary comparison against two values. These include the standard relation
 * operators, including =, !=, >, >=, < and <=. When the result couldn't be determined against two numerical values,
 * the state is split with both outcomes possible. With any other type, the result will be both 0 and 1 for each
 * numerical comparison without a determined result.
 */
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

        // Number <op> number is a special case, because then it's possible to apply the relation to the state
        if (leftType instanceof DfBigDecimalType && rightType instanceof DfBigDecimalType) {
            DfaCondition condition = dfaLeft.cond(relation, dfaRight);
            if (condition == DfaCondition.getTrue()) {
                pushResult(interpreter, stateBefore, fromValue(1));
                return nextStates(interpreter, stateBefore);
            }
            if (condition == DfaCondition.getFalse()) {
                pushResult(interpreter, stateBefore, fromValue(0));
                return nextStates(interpreter, stateBefore);
            }
            ArrayList<DfaInstructionState> states = new ArrayList<>(2);
            DfaMemoryState copy = stateBefore.createCopy();
            if (copy.applyCondition(condition)) {
                pushResult(interpreter, copy, fromValue(1));
                states.add(nextState(interpreter, copy));
            }
            copy = stateBefore.createCopy();
            if (copy.applyCondition(condition.negate())) {
                pushResult(interpreter, copy, fromValue(0));
                states.add(nextState(interpreter, copy));
            }
            if (states.isEmpty()) {
                pushResult(interpreter, stateBefore, fromValue(0));
                return nextStates(interpreter, stateBefore);
            }
            return states.toArray(DfaInstructionState.EMPTY_ARRAY);
        }

        DfaValue out = TypeEvaluator.evaluateBinaryOperator(interpreter.getFactory(), stateBefore, dfaLeft, dfaRight, (v1, v2) -> {
            DfaCondition.Exact value = DfaCondition.tryEvaluate(v1, relation, v2);
            if (value != null) return fromValue(value == DfaCondition.getTrue() ? 1 : 0);
            return fromRange(pointSet(BigDecimal.ZERO, BigDecimal.ONE));
        });
        pushResult(interpreter, stateBefore, out);
        return nextStates(interpreter, stateBefore);
    }

    @Override
    public String toString() {
        return "BINARY_OP " + relation.toString();
    }
}
