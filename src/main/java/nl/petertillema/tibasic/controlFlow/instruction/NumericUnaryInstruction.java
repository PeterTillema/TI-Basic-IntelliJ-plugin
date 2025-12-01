package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumericUnaryInstruction extends EvalInstruction {

    private final UnaryOperator operator;

    public NumericUnaryInstruction(@Nullable DfaAnchor anchor, @NotNull UnaryOperator operator) {
        super(anchor, 1);
        this.operator = operator;
    }

    @Override
    public @NotNull DfaValue eval(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        DfType numType = state.getDfType(arguments[0]);

        if (numType instanceof DfBigDecimalType bigDecimalType) {
            // todo
            return factory.getUnknown();
        }

        return factory.getUnknown();
    }

    @Override
    public String toString() {
        return "BINARY_OP " + operator.toString();
    }
}
