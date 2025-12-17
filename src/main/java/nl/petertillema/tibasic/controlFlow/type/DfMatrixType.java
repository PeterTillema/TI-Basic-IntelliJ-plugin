package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import org.jetbrains.annotations.NotNull;

public class DfMatrixType extends DfListType {

    public DfMatrixType(SpecialFieldDescriptor specialField, DfType specialFieldType) {
        super(specialField, specialFieldType);
    }

    @Override
    public @NotNull String toString() {
        return "matrix with length=" + getSpecialFieldType();
    }
}
