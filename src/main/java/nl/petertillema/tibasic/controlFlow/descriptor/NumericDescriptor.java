package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

public class NumericDescriptor extends TIBasicVariableDescriptor {

    public NumericDescriptor(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return fromRange(RangeSet.ALL);
    }

}
