package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Descriptor for single-character TI-BASIC variables (A–Z, theta/θ).
 * Uses the variable text as an identifier (e.g., "A", "B", "theta").
 */
public final class SimpleVariableDescriptor implements VariableDescriptor {

    private final @NotNull String name;

    public SimpleVariableDescriptor(@NotNull String name) {
        if ("θ".equals(name)) name = "theta";
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof SimpleVariableDescriptor other && Objects.equals(other.name, name));
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public @NotNull DfType getInitialDfType(@NotNull DfaVariableValue thisValue, @Nullable PsiElement context) {
        return DfType.TOP;
    }

    @Override
    public @NotNull DfaValue createValue(@NotNull DfaValueFactory factory, @Nullable DfaValue qualifier) {
        return VariableDescriptor.super.createValue(factory, qualifier);
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return DfType.TOP;
    }
}
