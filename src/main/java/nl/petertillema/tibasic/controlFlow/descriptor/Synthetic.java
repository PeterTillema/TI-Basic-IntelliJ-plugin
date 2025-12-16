package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Synthetic implements VariableDescriptor {

    private final int myLocation;
    private final DfType myType;

    private static final Random random = new Random();

    public Synthetic(int location, DfType type) {
        myLocation = location;
        myType = type;
    }

    public static Synthetic create() {
        int location = random.nextInt(Integer.MAX_VALUE - 10_000) + 10_000;
        return new Synthetic(location, DfType.TOP);
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
