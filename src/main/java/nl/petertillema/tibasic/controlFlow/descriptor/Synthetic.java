package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Synthetic implements VariableDescriptor {

    private final int myLocation;
    private final DfType myType;

    public Synthetic(int location, DfType type) {
        myLocation = location;
        myType = type;
    }

    @Override
    public @NotNull String toString() {
        return "tmp$" + myLocation;
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return myType;
    }

    @Override
    public boolean isStable() {
        return true;
    }

}
