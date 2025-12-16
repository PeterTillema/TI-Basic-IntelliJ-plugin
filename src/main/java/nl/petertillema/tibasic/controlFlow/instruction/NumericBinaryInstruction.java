package nl.petertillema.tibasic.controlFlow.instruction;

import com.intellij.codeInspection.dataFlow.lang.DfaAnchor;
import com.intellij.codeInspection.dataFlow.lang.ir.EvalInstruction;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.Synthetic;
import nl.petertillema.tibasic.controlFlow.type.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfElementMap;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfStringType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

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

        // Concat strings
        if (leftType instanceof DfStringType string1 && rightType instanceof DfStringType string2 && operator == BinaryOperator.PLUS) {
            String newText = string1.getText() + string2.getText();
            DfStringType stringType = (DfStringType) SpecialFieldDescriptor.STRING_LENGTH.asDfType(fromValue(newText.length()));
            stringType.setText(newText);
            return factory.fromDfType(stringType);
        }

        // Operation on lists and matrices
        if (leftType instanceof DfListType && rightType instanceof DfBigDecimalType bd) {
            DfElementMap elementMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[0]);
            return evalList(factory, state, elementMap, v -> v.eval(bd, operator));
        }
        if (leftType instanceof DfBigDecimalType bd && rightType instanceof DfListType) {
            DfElementMap elementMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[1]);
            // Number/matrix is an invalid operation
            if (elementMap.getDimensions() == 2 && operator == BinaryOperator.DIVIDE) return factory.getUnknown();
            return evalList(factory, state, elementMap, v -> bd.eval(v, operator));
        }
        if (leftType instanceof DfListType && rightType instanceof DfListType) {
            DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[0]);
            DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[1]);
            DfaVariableValue outputList = factory.getVarFactory().createVariableValue(Synthetic.create());
            // Special operations for TIMES and DIVIDE
            DfElementMap outputMap;
            if (leftMap.getDimensions() == 2 && operator == BinaryOperator.TIMES) {
                outputMap = leftMap.matrixMultiply(rightMap);
            } else if (leftMap.getDimensions() == 2 && operator == BinaryOperator.DIVIDE) {
                outputMap = leftMap.matrixMultiply(rightMap.inverse());
            } else {
                outputMap = leftMap.execBiOperator(rightMap, (v1, v2) -> v1.eval(v2, operator));
            }
            outputMap.exportTo((DfaMemoryStateImpl) state, outputList);
            return outputList;
        }

        // Operation on numerical values
        if (leftType instanceof DfBigDecimalType bd1 && rightType instanceof DfBigDecimalType bd2) {
            return factory.fromDfType(bd1.eval(bd2, operator));
        }

        return factory.getUnknown();
    }

    private DfaVariableValue evalList(DfaValueFactory factory, DfaMemoryState state, DfElementMap elementMap, Function<DfBigDecimalType, DfType> op) {
        DfaVariableValue outputList = factory.getVarFactory().createVariableValue(Synthetic.create());
        elementMap.execOperator(op).exportTo((DfaMemoryStateImpl) state, outputList);
        return outputList;
    }

    @Override
    public String toString() {
        return "NUMERIC_OP " + operator.toString();
    }
}
