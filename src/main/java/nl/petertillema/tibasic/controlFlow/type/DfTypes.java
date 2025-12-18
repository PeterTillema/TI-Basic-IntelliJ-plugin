package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DfTypes {

    public static final DfBooleanType BOOLEAN = new DfBooleanType() {
        @Override
        public boolean isSuperType(@NotNull DfType other) {
            return other == BOTTOM || other instanceof DfBooleanType;
        }

        @Override
        public @NotNull DfType join(@NotNull DfType other) {
            return other instanceof DfBooleanType ? this : TOP;
        }

        @Override
        public @Nullable DfType tryJoinExactly(@NotNull DfType other) {
            return other instanceof DfBooleanType ? this : null;
        }

        @Override
        public @NotNull DfType meet(@NotNull DfType other) {
            if (other == TOP) return this;
            if (other instanceof DfBooleanType) return other;
            return BOTTOM;
        }

        @Override
        public @NotNull DfType tryNegate() {
            return BOTTOM;
        }

        @Override
        public int hashCode() {
            return 345661;
        }

        @Override
        public @NotNull String toString() {
            return "bool";
        }
    };

    public static final @NotNull DfBooleanConstantType TRUE = new DfBooleanConstantType(true);

    public static final @NotNull DfBooleanConstantType FALSE = new DfBooleanConstantType(false);

}
