package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

public class ListDescriptor extends TIBasicVariableDescriptor {
    public ListDescriptor(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return new DfListType(SpecialFieldDescriptor.LIST_LENGTH, fromRange(new Range(BigDecimal.ZERO, BigDecimal.valueOf(999))));
    }
}
