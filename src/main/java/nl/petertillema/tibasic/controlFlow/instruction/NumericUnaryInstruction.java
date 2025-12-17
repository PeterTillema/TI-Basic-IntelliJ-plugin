package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.Synthetic;
import nl.petertillema.tibasic.controlFlow.operator.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.operator.UnaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfElementMap;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.FULL_RANGE;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

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
            return evalNumber(factory, bigDecimalType);
        }

        if (numType instanceof DfListType || numType instanceof DfMatrixType) {
            DfElementMap elementMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[0]);

            if (operator == UnaryOperator.TRANSPOSE && elementMap.getDimensions() == 2) {
                DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
                elementMap.transpose().exportTo((DfaMemoryStateImpl) state, out);
                return out;
            }
            if (operator == UnaryOperator.INVERSE && elementMap.getDimensions() == 2) {
                DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
                elementMap.inverse().exportTo((DfaMemoryStateImpl) state, out);
                return out;
            }

            // Element-wise operations (lists and/or matrices depending on operator)
            int dims = elementMap.getDimensions();
            switch (operator) {
                case NEG: {
                    DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
                    DfElementMap res = elementMap.execOperator(v -> v.eval(fromValue(-1), BinaryOperator.TIMES));
                    res.exportTo((DfaMemoryStateImpl) state, out);
                    return out;
                }
                case INVERSE: {
                    DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
                    DfElementMap res = elementMap.execOperator(v -> ((DfBigDecimalType) fromValue(1)).eval(v, BinaryOperator.DIVIDE));
                    res.exportTo((DfaMemoryStateImpl) state, out);
                    return out;
                }
                case FACTORIAL: {
                    if (dims != 1) return factory.getUnknown();
                    DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
                    DfElementMap res = elementMap.execOperator(v -> fromRange(FULL_RANGE));
                    res.exportTo((DfaMemoryStateImpl) state, out);
                    return out;
                }
                case TO_RADIAN: {
                    // todo: check state for Radian/Degree mode
                    return factory.getUnknown();
                }
                case TO_DEGREE: {
                    // todo: check state for Radian/Degree mode
                    return factory.getUnknown();
                }
            }
        }

        return factory.getUnknown();
    }

    private @NotNull DfaValue evalNumber(@NotNull DfaValueFactory factory, @NotNull DfBigDecimalType v) {
        return switch (operator) {
            case NEG -> factory.fromDfType(v.eval(fromValue(-1), BinaryOperator.TIMES));
            case INVERSE -> factory.fromDfType(((DfBigDecimalType) fromValue(1)).eval(v, BinaryOperator.DIVIDE));
            case TO_RADIAN -> {
                // todo: check state for Radian/Degree mode
                yield factory.getUnknown();
            }
            case TO_DEGREE -> {
                // todo: check state for Radian/Degree mode
                yield factory.getUnknown();
            }
            case FACTORIAL -> factory.fromDfType(fromRange(FULL_RANGE));
            case TRANSPOSE -> factory.getUnknown();
        };
    }

    @Override
    public String toString() {
        return "UNARY_OP " + operator.toString();
    }
}
