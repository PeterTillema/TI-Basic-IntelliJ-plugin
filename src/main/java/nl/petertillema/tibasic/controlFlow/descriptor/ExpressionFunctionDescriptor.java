package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import nl.petertillema.tibasic.controlFlow.type.DfDoubleRangeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Descriptor for zero-argument expression functions like "rand" or "getKey".
 * Identity is based on the function name only; an optional domain (NumberRangeDfType)
 * can be provided to expose a useful data-flow type.
 */
public final class ExpressionFunctionDescriptor implements VariableDescriptor {

    public static final DfType RAND_DOMAIN = DfDoubleRangeType.create(0.0, 1.0, false, false);
    public static final DfType GETKEY_DOMAIN = DfDoubleRangeType.create(0.0, 105.0, false, false);

    private final @NotNull String name;
    private final @Nullable DfType domain;

    public ExpressionFunctionDescriptor(@NotNull String name, @Nullable DfType domain) {
        this.name = name;
        this.domain = domain;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable DfType getDomain() {
        return domain;
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return domain != null ? domain : DfType.TOP;
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String toString() {
        return domain == null ? name : name + ":" + domain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof ExpressionFunctionDescriptor other && Objects.equals(other.name, name));
    }
}
