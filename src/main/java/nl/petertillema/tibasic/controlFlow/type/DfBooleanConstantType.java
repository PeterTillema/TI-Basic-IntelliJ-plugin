package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfConstantType;
import com.intellij.codeInspection.dataFlow.types.DfType;
import org.jetbrains.annotations.NotNull;

public class DfBooleanConstantType extends DfConstantType<Boolean> implements DfBooleanType {

    public DfBooleanConstantType(boolean value) {
        super(value);
    }

    @Override
    public @NotNull DfType join(@NotNull DfType other) {
        if (other.equals(this)) return this;
        if (other instanceof DfBooleanType) return DfTypes.BOOLEAN;
        return DfType.TOP;
    }

    @Override
    public @NotNull DfType tryJoinExactly(@NotNull DfType other) {
        return join(other);
    }

    @Override
    public @NotNull DfType tryNegate() {
        return getValue() ? DfTypes.FALSE : DfTypes.TRUE;
    }

}
