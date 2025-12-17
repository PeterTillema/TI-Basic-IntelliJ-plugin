package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DerivedVariableDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DfStringType implements DfType {

    private final SpecialFieldDescriptor specialField;
    private final DfType specialFieldType;

    private String text = "";

    public DfStringType(SpecialFieldDescriptor specialField, DfType specialFieldType) {
        this.specialField = specialField;
        this.specialFieldType = specialFieldType;
    }

    @Override
    public boolean isSuperType(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return true;
        if (!(other instanceof DfStringType o)) return false;
        if (this.specialField != o.specialField) return false;
        return this.specialFieldType.isSuperType(o.specialFieldType);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public @NotNull DfType join(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return this;
        if (!(other instanceof DfStringType o)) return DfType.TOP;
        if (this.specialField != o.specialField) return DfType.TOP;
        DfType joinedField = this.specialFieldType.join(o.specialFieldType);
        return specialField.asDfType(joinedField);
    }

    @Override
    public @Nullable DfType tryJoinExactly(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return this;
        if (other == DfType.TOP) return other;
        if (!(other instanceof DfStringType o)) return null;
        if (this.specialField != o.specialField) return null;
        DfType joinedField = this.specialFieldType.tryJoinExactly(o.specialFieldType);
        if (joinedField == null) return null;
        return specialField.asDfType(joinedField);
    }

    @Override
    public @NotNull DfType meet(@NotNull DfType other) {
        if (other == DfType.TOP) return this;
        if (!(other instanceof DfStringType o)) return DfType.BOTTOM;
        if (this.specialField != o.specialField) return DfType.BOTTOM;
        DfType metField = this.specialFieldType.meet(o.specialFieldType);
        return specialField.asDfType(metField);
    }

    @Override
    public @NotNull List<@NotNull DerivedVariableDescriptor> getDerivedVariables() {
        return List.of(specialField);
    }

    @Override
    public @NotNull Map<@NotNull DerivedVariableDescriptor, @NotNull DfType> getDerivedValues() {
        return Map.of(specialField, specialFieldType);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public @NotNull String toString() {
        return "string with length=" + specialFieldType;
    }
}
