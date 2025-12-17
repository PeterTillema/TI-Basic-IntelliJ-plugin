package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.Synthetic;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class TypeEvaluator {

    public static DfaValue evaluateBinaryOperator(@NotNull DfaValueFactory factory,
                                                  @NotNull DfaMemoryState state,
                                                  @NotNull DfaValue leftValue,
                                                  @NotNull DfaValue rightValue,
                                                  @NotNull BiFunction<DfBigDecimalType, DfBigDecimalType, DfType> function) {
        DfType leftType = state.getDfType(leftValue);
        DfType rightType = state.getDfType(rightValue);

        if ((leftType instanceof DfListType || leftType instanceof DfMatrixType) && (rightType instanceof DfListType || rightType instanceof DfMatrixType)) {
            DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) leftValue);
            DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) rightValue);
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            leftMap.execBiOperator(rightMap, function).exportTo((DfaMemoryStateImpl) state, out);
            return out;
        } else if ((leftType instanceof DfListType || leftType instanceof DfMatrixType) && rightType instanceof DfBigDecimalType bigDecimalType) {
            DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) leftValue);
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            leftMap.execOperator(value -> function.apply(value, bigDecimalType)).exportTo((DfaMemoryStateImpl) state, out);
            return out;
        } else if (leftType instanceof DfBigDecimalType bigDecimalType && (rightType instanceof DfListType || rightType instanceof DfMatrixType)) {
            DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) rightValue);
            DfaVariableValue out = factory.getVarFactory().createVariableValue(Synthetic.create());
            rightMap.execOperator(value -> function.apply(bigDecimalType, value)).exportTo((DfaMemoryStateImpl) state, out);
            return out;
        } else if (leftType instanceof DfBigDecimalType bigDecimalType1 && rightType instanceof DfBigDecimalType bigDecimalType2) {
            return factory.fromDfType(function.apply(bigDecimalType1, bigDecimalType2));
        }
        return factory.getUnknown();
    }

}
