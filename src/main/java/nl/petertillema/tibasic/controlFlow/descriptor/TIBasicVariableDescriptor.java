package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A descriptor describes a variable within the DFA engine. TI-BASIC variables are always distinguished by their name,
 * and multiple types of descriptors are possible within the language, namely all the subclasses from this abstract
 * class. Each type of descriptor should implement the getDfType() function, which returns one of the possible types in
 * TI-BASIC, represented by any variable described by the descriptor. For example, a variable with a numeric descriptor
 * holds a DfType of any value between -9.99999999999e99 and +9.99999999999e99. Some descriptors have a more precise
 * range. For example, a list descriptor always has a length between 0 and 999, since larger is not allowed in the
 * language. Since variable names can be written in different ways, the name is normalized to the ASCII range, such
 * that different writings of a variable are always equal to each other.
 */
public abstract class TIBasicVariableDescriptor implements VariableDescriptor {

    private final @NotNull String name;

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
    public abstract @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier);

    @Override
    public @NotNull DfaValue createValue(@NotNull DfaValueFactory factory, @Nullable DfaValue qualifier) {
        return factory.getVarFactory().createVariableValue(this);
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
