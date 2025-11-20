package nl.petertillema.tibasic.tokenization;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public record TokenBytes(byte[] bytes) {

    @Override
    public int hashCode() {
        int hash = 0;
        for (byte aByte : bytes) {
            hash = hash * 256 + (int) aByte;
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenBytes that = (TokenBytes) o;
        return Objects.deepEquals(bytes, that.bytes);
    }

    @Override
    public @NotNull String toString() {
        return "TokenBytes" + Arrays.toString(bytes);
    }
}
