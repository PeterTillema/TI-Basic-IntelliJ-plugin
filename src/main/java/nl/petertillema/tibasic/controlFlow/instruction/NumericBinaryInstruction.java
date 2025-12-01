package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.controlFlow.type.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumericBinaryInstruction extends EvalInstruction {

    private final BinaryOperator operator;

    public NumericBinaryInstruction(@Nullable DfaAnchor anchor, @NotNull BinaryOperator operator) {
        super(anchor, 2);
        this.operator = operator;
    }

    @Override
    public @NotNull DfaValue eval(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        DfType leftType = state.getDfType(arguments[0]);
        DfType rightType = state.getDfType(arguments[1]);

        if (leftType instanceof DfBigDecimalType bd1 && rightType instanceof DfBigDecimalType bd2) {
            return factory.fromDfType(bd1.eval(bd2, operator));
        }

        return factory.getUnknown();
    }

    @Override
    public String toString() {
        return "NUMERIC_OP " + operator.toString();
    }
}
