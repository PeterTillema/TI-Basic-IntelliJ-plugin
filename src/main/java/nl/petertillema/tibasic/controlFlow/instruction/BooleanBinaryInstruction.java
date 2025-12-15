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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;

public class BooleanBinaryInstruction extends ExpressionPushingInstruction {

    private final RelationType operator;

    public BooleanBinaryInstruction(@Nullable DfaAnchor anchor, @NotNull RelationType operator) {
        super(anchor);
        this.operator = operator;
    }

    @Override
    public DfaInstructionState[] accept(@NotNull DataFlowInterpreter interpreter, @NotNull DfaMemoryState stateBefore) {
        DfaValue dfaRight = stateBefore.pop();
        DfaValue dfaLeft = stateBefore.pop();

        if (operator == RelationType.EQ || operator == RelationType.NE) {
            ArrayList<DfaInstructionState> states = new ArrayList<>(2);
            DfaMemoryState equality = stateBefore.createCopy();
            DfaCondition condition = dfaLeft.eq(dfaRight);
            if (equality.applyCondition(condition)) {
                pushResult(interpreter, equality, fromRange(pointSet(Set.of(BigDecimal.ZERO, BigDecimal.ONE))));
                states.add(nextState(interpreter, equality));
            }
            if (stateBefore.applyCondition(condition.negate())) {
                pushResult(interpreter, stateBefore, fromValue(operator == RelationType.NE ? 1 : 0));
                states.add(nextState(interpreter, stateBefore));
            }
            return states.toArray(DfaInstructionState.EMPTY_ARRAY);
        }
        RelationType[] relations = splitRelation(operator);

        ArrayList<DfaInstructionState> states = new ArrayList<>(relations.length);

        for (int i = 0; i < relations.length; i++) {
            RelationType relation = relations[i];
            boolean result = operator.isSubRelation(relation);
            DfaCondition condition = dfaLeft.cond(relation, dfaRight).correctForRelationResult(result);
            if (condition == DfaCondition.getFalse()) continue;
            if (condition == DfaCondition.getTrue()) {
                pushResult(interpreter, stateBefore, fromValue(result ? 1 : 0));
                return nextStates(interpreter, stateBefore);
            }
            final DfaMemoryState copy = i == relations.length - 1 && !states.isEmpty() ? stateBefore : stateBefore.createCopy();
            if (copy.applyCondition(condition) &&
                    copy.meetDfType(dfaLeft, copy.getDfType(dfaLeft).correctForRelationResult(operator, result)) &&
                    copy.meetDfType(dfaRight, copy.getDfType(dfaRight).correctForRelationResult(operator, result))) {
                pushResult(interpreter, copy, fromValue(result ? 1 : 0));
                states.add(nextState(interpreter, copy));
            }
        }
        if (states.isEmpty()) {
            // Neither of relations could be applied: likely comparison with NaN; do not split the state in this case, just push false
            pushResult(interpreter, stateBefore, fromValue(0));
            return nextStates(interpreter, stateBefore);
        }

        return states.toArray(DfaInstructionState.EMPTY_ARRAY);
    }

    private static RelationType @NotNull [] splitRelation(RelationType relationType) {
        return switch (relationType) {
            case LT, LE, GT, GE -> new RelationType[]{RelationType.LT, RelationType.GT, RelationType.EQ};
            default -> new RelationType[]{relationType, relationType.getNegated()};
        };
    }

    @Override
    public String toString() {
        return "BINARY_OP " + operator.toString();
    }
}
