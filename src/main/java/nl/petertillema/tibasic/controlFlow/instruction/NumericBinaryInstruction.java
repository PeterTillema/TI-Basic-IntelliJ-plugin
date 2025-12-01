package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import nl.petertillema.tibasic.controlFlow.type.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfDoubleConstantType;
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

        if (leftType instanceof DfDoubleConstantType d1 && rightType instanceof DfDoubleConstantType d2) {
            return factory.fromDfType(eval(operator, d1.getValue(), d2.getValue()));
        }
        return factory.getUnknown();
    }

    private static DfType eval(BinaryOperator op, double d1, double d2) {
        return switch (op) {
            case PLUS -> new DfDoubleConstantType(d1 + d2).makeWide();
            case MINUS -> new DfDoubleConstantType(d1 - d2).makeWide();
            case TIMES -> new DfDoubleConstantType(d1 * d2).makeWide();
            case DIVIDE -> new DfDoubleConstantType(d1 / d2).makeWide();
            case POW -> new DfDoubleConstantType(Math.pow(d1, d2)).makeWide();
        };
    }

    @Override
    public String toString() {
        return "NUMERIC_OP " + operator.toString();
    }
}
