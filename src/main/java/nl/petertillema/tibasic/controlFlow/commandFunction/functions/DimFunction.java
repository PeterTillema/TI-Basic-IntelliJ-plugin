package nl.petertillema.tibasic.controlFlow.commandFunction.functions;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.Synthetic;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfElementMap;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

public class DimFunction implements TIBasicFunction {

    @Override
    public int getMinNrArguments() {
        return 1;
    }

    @Override
    public int getMaxNrArguments() {
        return 1;
    }

    @Override
    public DfaValue evalFunction(@NotNull DfaValueFactory factory, @NotNull DfaMemoryState state, @NotNull DfaValue @NotNull ... arguments) {
        DfType varType = state.getDfType(arguments[0]);
        if (varType instanceof DfMatrixType) {
            DfElementMap map = DfElementMap.loadFromSource((DfaMemoryStateImpl) state, (DfaVariableValue) arguments[0]);
            List<DfType> dimensionLengths = map.getDimensionLengths();
            Map<Integer, Map<Integer, DfBigDecimalType>> dimensionListElements = Map.of(
                    1, Map.of(1, (DfBigDecimalType) dimensionLengths.getFirst()),
                    2, Map.of(1, (DfBigDecimalType) dimensionLengths.get(1))
            );
            DfElementMap dimsMap = new DfElementMap(1, List.of(fromValue(2)), dimensionListElements);
            DfaVariableValue outList = factory.getVarFactory().createVariableValue(Synthetic.create());
            dimsMap.exportTo((DfaMemoryStateImpl) state, outList);
            return outList;
        }
        if (varType instanceof DfListType listType) {
            return factory.fromDfType(listType.getSpecialFieldType());
        }
        return factory.getUnknown();
    }
}
