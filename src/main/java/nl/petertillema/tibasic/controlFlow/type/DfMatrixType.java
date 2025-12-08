package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DfMatrixType implements DfType {

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
    public @NotNull String toString() {
        return "matrix";
    }
}
