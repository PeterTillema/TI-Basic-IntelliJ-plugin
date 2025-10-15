package nl.petertillema.tibasic.editor;

import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_COLORS;

public final class TIBasicElementColorProvider implements ElementColorProvider {

    @Override
    public @Nullable Color getColorFrom(@NotNull PsiElement element) {
        var node = element.getNode();
        if (node.getElementType() != TIBasicTypes.COLOR_VARIABLE) return null;
        return TIBASIC_COLORS.get(node.getText());
    }

    @Override
    public void setColorTo(@NotNull PsiElement element, @NotNull Color color) {
        // Not implemented, the colors aren't arbitrary
    }

}
