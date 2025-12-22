package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfStringType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet.range;

public class StringDescriptor extends TIBasicVariableDescriptor {

    public StringDescriptor(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return new DfStringType(SpecialFieldDescriptor.STRING_LENGTH, fromRange(range(BigDecimal.ZERO, BigDecimal.valueOf(999))));
    }

}
