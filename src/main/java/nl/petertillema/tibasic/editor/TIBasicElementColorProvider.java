package nl.petertillema.tibasic.editor;

import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;

public class TIBasicElementColorProvider implements ElementColorProvider {

    private static final Map<String, Color> TIBASIC_COLORS = Map.ofEntries(

        Map.entry("BLUE", new JBColor(new Color(0, 0, 255), new Color(0, 0, 255))),
        Map.entry("Blue", new JBColor(new Color(0, 0, 255), new Color(0, 0, 255))),
        Map.entry("RED", new JBColor(new Color(255, 0, 0), new Color(255, 0, 0))),
        Map.entry("Red", new JBColor(new Color(255, 0, 0), new Color(255, 0, 0))),
        Map.entry("BLACK", Gray._0),
        Map.entry("Black", Gray._0),
        Map.entry("MAGENTA", new JBColor(new Color(255, 0, 255), new Color(255, 0, 255))),
        Map.entry("Magenta", new JBColor(new Color(255, 0, 255), new Color(255, 0, 255))),
        Map.entry("GREEN", new JBColor(new Color(0, 156, 0), new Color(0, 156, 0))),
        Map.entry("Green", new JBColor(new Color(0, 156, 0), new Color(0, 156, 0))),
        Map.entry("ORANGE", new JBColor(new Color(255, 140, 32), new Color(255, 140, 32))),
        Map.entry("Orange", new JBColor(new Color(255, 140, 32), new Color(255, 140, 32))),
        Map.entry("BROWN", new JBColor(new Color(176, 32, 0), new Color(176, 32, 0))),
        Map.entry("Brown", new JBColor(new Color(176, 32, 0), new Color(176, 32, 0))),
        Map.entry("NAVY", new JBColor(new Color(0, 0, 120), new Color(0, 0, 120))),
        Map.entry("Navy", new JBColor(new Color(0, 0, 120), new Color(0, 0, 120))),
        Map.entry("LTBLUE", new JBColor(new Color(0, 144, 255), new Color(0, 144, 255))),
        Map.entry("LtBlue", new JBColor(new Color(0, 144, 255), new Color(0, 144, 255))),
        Map.entry("YELLOW", new JBColor(new Color(255, 255, 0), new Color(255, 255, 0))),
        Map.entry("Yellow", new JBColor(new Color(255, 255, 0), new Color(255, 255, 0))),
        Map.entry("WHITE", Gray._255),
        Map.entry("White", Gray._255),
        Map.entry("LTGRAY", Gray._224),
        Map.entry("LtGray", Gray._224),
        Map.entry("LTGREY", Gray._224),
        Map.entry("LtGrey", Gray._224),
        Map.entry("MEDGRAY", Gray._192),
        Map.entry("MedGray", Gray._192),
        Map.entry("MEDGREY", Gray._192),
        Map.entry("MedGrey", Gray._192),
        Map.entry("GRAY", Gray._136),
        Map.entry("Gray", Gray._136),
        Map.entry("GREY", Gray._136),
        Map.entry("Grey", Gray._136),
        Map.entry("DARKGRAY", Gray._84),
        Map.entry("DarkGray", Gray._84),
        Map.entry("DARKGREY", Gray._84),
        Map.entry("DarkGrey", Gray._84)
    );

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
