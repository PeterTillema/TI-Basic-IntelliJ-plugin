package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A descriptor which represents a boolean value. A true boolean value doesn't exist in TI-BASIC, as the result of a
 * logical or boolean binary comparison results in a 0 or 1, but this descriptor can be used for setting variables.
 */
public class BooleanDescriptor extends TIBasicVariableDescriptor {

    public static final BooleanDescriptor RADIAN_VARIABLE = new BooleanDescriptor("Radian");

    public BooleanDescriptor(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return DfTypes.BOOLEAN;
    }

}
