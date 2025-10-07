package nl.petertillema.tibasic.psi;

import com.intellij.psi.tree.IElementType;
import nl.petertillema.tibasic.language.TIBasicLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicElementType extends IElementType {

    public TIBasicElementType(@NotNull @NonNls String debugName) {
        super(debugName, TIBasicLanguage.INSTANCE);
    }

}
