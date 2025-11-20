package nl.petertillema.tibasic;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class TIBasicMessageBundle {

    public static final @NonNls String BUNDLE = "messages.TIBasicBundle";
    private static final DynamicBundle INSTANCE = new DynamicBundle(TIBasicMessageBundle.class, BUNDLE);

    private TIBasicMessageBundle() {
    }

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

}
