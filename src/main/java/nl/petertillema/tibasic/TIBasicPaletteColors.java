package nl.petertillema.tibasic;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class TIBasicPaletteColors {

    public static final JBColor TRANSPARENT = new JBColor(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0));
    public static final JBColor BLUE = new JBColor(new Color(0, 0, 255), new Color(0, 0, 255));
    public static final JBColor RED = new JBColor(new Color(255, 0, 0), new Color(255, 0, 0));
    public static final JBColor BLACK = new JBColor(new Color(0, 0, 0), new Color(0, 0, 0));
    public static final JBColor MAGENTA = new JBColor(new Color(255, 0, 255), new Color(255, 0, 255));
    public static final JBColor GREEN = new JBColor(new Color(0, 156, 0), new Color(0, 156, 0));
    public static final JBColor ORANGE = new JBColor(new Color(255, 140, 32), new Color(255, 140, 32));
    public static final JBColor BROWN = new JBColor(new Color(176, 32, 0), new Color(176, 32, 0));
    public static final JBColor NAVY = new JBColor(new Color(0, 0, 120), new Color(0, 0, 120));
    public static final JBColor LIGHT_BLUE = new JBColor(new Color(0, 144, 255), new Color(0, 144, 255));
    public static final JBColor YELLOW = new JBColor(new Color(255, 255, 0), new Color(255, 255, 0));
    public static final JBColor WHITE = new JBColor(new Color(255, 255, 255), new Color(255, 255, 255));
    public static final JBColor LIGHT_GRAY = new JBColor(new Color(224, 224, 224), new Color(224, 224, 224));
    public static final JBColor MED_GRAY = new JBColor(new Color(192, 192, 192), new Color(192, 192, 192));
    public static final JBColor GRAY = new JBColor(new Color(136, 136, 136), new Color(136, 136, 136));
    public static final JBColor DARK_GRAY = new JBColor(new Color(84, 84, 84), new Color(84, 84, 84));

    public static final Map<String, Color> TIBASIC_COLORS = Map.ofEntries(
            Map.entry("BLUE", BLUE),
            Map.entry("Blue", BLUE),
            Map.entry("RED", RED),
            Map.entry("Red", RED),
            Map.entry("BLACK", BLACK),
            Map.entry("Black", BLACK),
            Map.entry("MAGENTA", MAGENTA),
            Map.entry("Magenta", MAGENTA),
            Map.entry("GREEN", GREEN),
            Map.entry("Green", GREEN),
            Map.entry("ORANGE", ORANGE),
            Map.entry("Orange", ORANGE),
            Map.entry("BROWN", BROWN),
            Map.entry("Brown", BROWN),
            Map.entry("NAVY", NAVY),
            Map.entry("Navy", NAVY),
            Map.entry("LTBLUE", LIGHT_BLUE),
            Map.entry("LtBlue", LIGHT_BLUE),
            Map.entry("YELLOW", YELLOW),
            Map.entry("Yellow", YELLOW),
            Map.entry("WHITE", WHITE),
            Map.entry("White", WHITE),
            Map.entry("LTGRAY", LIGHT_GRAY),
            Map.entry("LtGray", LIGHT_GRAY),
            Map.entry("LTGREY", LIGHT_GRAY),
            Map.entry("LtGrey", LIGHT_GRAY),
            Map.entry("MEDGRAY", MED_GRAY),
            Map.entry("MedGray", MED_GRAY),
            Map.entry("MEDGREY", MED_GRAY),
            Map.entry("MedGrey", MED_GRAY),
            Map.entry("GRAY", GRAY),
            Map.entry("Gray", GRAY),
            Map.entry("GREY", GRAY),
            Map.entry("Grey", GRAY),
            Map.entry("DARKGRAY", DARK_GRAY),
            Map.entry("DarkGray", DARK_GRAY),
            Map.entry("DARKGREY", DARK_GRAY),
            Map.entry("DarkGrey", DARK_GRAY)
    );

    public static final List<Color> TIBASIC_PALETTE = List.of(
            TRANSPARENT,
            BLUE,
            RED,
            BLACK,
            MAGENTA,
            GREEN,
            ORANGE,
            BROWN,
            NAVY,
            LIGHT_BLUE,
            YELLOW,
            WHITE,
            LIGHT_GRAY,
            MED_GRAY,
            GRAY,
            DARK_GRAY
    );

    public static final List<Color> MONOCHROME_PALETTE = List.of(
            new JBColor(new Color(255, 255, 255), new Color(255, 255, 255)),
            new JBColor(new Color(0x6B, 0x6D, 0x5A), new Color(0x6B, 0x6D, 0x5A))
    );

}
