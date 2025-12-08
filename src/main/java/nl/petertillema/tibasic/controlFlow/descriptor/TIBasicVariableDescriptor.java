package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * This is a descriptor for all built-in TI-BASIC variables as well as custom lists. All the variables have a name,
 * such as "A", "L1", "Str0" or "|LABC". The names are always normalized to the default ASCII representation, so "₁"
 * will be replaced with "1", and custom lists should always have a "|L" prefix. The descriptor itself
 * can serve as a qualifier for related nested variables, for example, the length of a list, or the row of a matrix.
 * The DFA engine can track these as separate {@link DfaVariableValue}s.
 */
public class TIBasicVariableDescriptor implements VariableDescriptor {

    private final @NotNull String name;

    /**
     * @param name The name of the TI-BASIC variable. Non-ascii characters are replaced by the ASCII representation,
     *             and the text is eventually extracted from inside {} braces.
     */
    public TIBasicVariableDescriptor(@NotNull String name) {
        name = name.replace("₁", "1")
                .replace("₂", "2")
                .replace("₃", "3")
                .replace("₄", "4")
                .replace("₅", "5")
                .replace("₆", "6")
                .replace("₇", "7")
                .replace("₈", "8")
                .replace("₉", "9")
                .replace("₀", "0")
                .replace("θ", "theta");
        if (name.startsWith("{") && name.endsWith("}")) name = name.substring(1, name.length() - 2);
        this.name = name;
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return DfType.TOP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TIBasicVariableDescriptor that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public @NotNull String toString() {
        return name;
    }

    public @NotNull String getName() {
        return name;
    }

}
