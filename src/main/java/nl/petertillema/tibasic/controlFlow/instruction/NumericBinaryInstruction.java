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
import nl.petertillema.tibasic.controlFlow.operator.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfElementMap;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.DfStringType;
import nl.petertillema.tibasic.controlFlow.type.TypeEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

/**
 * An instruction which performs a numeric binary operation against two values. These include the standard operators,
 * including "+", "-", "*", "/" and "^". Some special cases that are different from the default operators:
 *  - string + string -> string concatenation
 *  - number / matrix -> not allowed
 *  - matrix * matrix -> matrix multiplication
 *  - matrix / matrix -> matrix division
 * All the other operators are applied normally, or element-wise for lists and matrices.
 */
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

        switch (leftType) {
            // Concat strings
            case DfStringType string1 when rightType instanceof DfStringType string2 && operator == BinaryOperator.PLUS -> {
                String newText = string1.getText() + string2.getText();
                DfStringType stringType = (DfStringType) SpecialFieldDescriptor.STRING_LENGTH.asDfType(fromValue(newText.length()));
                stringType.setText(newText);
                return factory.fromDfType(stringType);
            }

            // Number/matrix is not allowed
            case DfBigDecimalType ignored when (rightType instanceof DfMatrixType && operator == BinaryOperator.DIVIDE) -> {
                return factory.getUnknown();
            }

            // Special operations for TIMES and DIVIDE
            case DfMatrixType ignored when (rightType instanceof DfMatrixType && operator == BinaryOperator.TIMES || operator == BinaryOperator.DIVIDE) -> {
                DfElementMap leftMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[0]);
                DfElementMap rightMap = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[1]);
                DfaVariableValue outputList = factory.getVarFactory().createVariableValue(Synthetic.create());
                leftMap.matrixMultiply(operator == BinaryOperator.TIMES ? rightMap : rightMap.inverse()).exportTo((DfaMemoryStateImpl) state, outputList);
                return outputList;
            }
            default -> {
                return TypeEvaluator.evaluateBinaryOperator(factory, state, arguments[0], arguments[1], (v1, v2) -> v1.eval(v2, operator));
            }
        }
    }

    @Override
    public String toString() {
        return "NUMERIC_OP " + operator.toString();
    }
}
