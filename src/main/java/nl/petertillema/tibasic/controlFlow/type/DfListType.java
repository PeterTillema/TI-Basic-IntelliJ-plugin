package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DfListType implements DfType {

    private final SpecialFieldDescriptor specialField;
    private final DfType specialFieldType;

    public DfListType(SpecialFieldDescriptor specialField, DfType specialFieldType) {
        this.specialField = specialField;
        this.specialFieldType = specialFieldType;
    }

    @Override
    public boolean isSuperType(@NotNull DfType other) {
        return false;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public @NotNull DfType join(@NotNull DfType other) {
        return this;
    }

    @Override
    public @Nullable DfType tryJoinExactly(@NotNull DfType other) {
        return this;
    }

    @Override
    public @NotNull DfType meet(@NotNull DfType other) {
        return this;
    }

    public DfType getSpecialFieldType() {
        return specialFieldType;
    }

    @Override
    public @NotNull String toString() {
        return "list";
    }
}
