package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DerivedVariableDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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

    @Override
    public @NotNull List<@NotNull DerivedVariableDescriptor> getDerivedVariables() {
        return List.of(specialField);
    }

    @Override
    public @NotNull Map<@NotNull DerivedVariableDescriptor, @NotNull DfType> getDerivedValues() {
        return Map.of(specialField, specialFieldType);
    }

    public DfType getSpecialFieldType() {
        return specialFieldType;
    }

    @Override
    public @NotNull String toString() {
        return "list with length=" + specialFieldType;
    }
}
