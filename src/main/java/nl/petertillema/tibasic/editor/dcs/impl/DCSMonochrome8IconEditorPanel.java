package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSIconEditorPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.MONOCHROME_PALETTE;

public class DCSMonochrome8IconEditorPanel extends AbstractDCSIconEditorPanel {

    private final char[] data = new char[16];

    public DCSMonochrome8IconEditorPanel(String iconData) {
        super(false);

        // Initialize data from the input
        Arrays.fill(data, '0');
        if (iconData != null && !iconData.isEmpty()) {
            int len = Math.min(iconData.length(), data.length);
            for (int i = 0; i < len; i++) {
                char ch = Character.toUpperCase(iconData.charAt(i));
                if (Character.digit(ch, 16) >= 0) data[i] = ch;
            }
        }
    }

    @Override
    protected int getGridSize() {
        return 8;
    }

    @Override
    protected String getIconData() {
        return new String(data);
    }

    @Override
    protected List<Color> getPalette() {
        return MONOCHROME_PALETTE;
    }

    @Override
    protected int getData(int index) {
        var nibble = Character.digit(this.data[index / 4], 16);
        var mask = 1 << (index % 4);
        return (nibble & mask) == 0 ? 0 : 1;
    }

    @Override
    protected void setData(int index, int paletteIndex) {
        var nibble = Character.digit(this.data[index / 4], 16);
        var mask = 1 << (index % 4);
        nibble = paletteIndex == 0 ? (nibble & ~mask) : (nibble | mask);
        this.data[index / 4] = toHex(nibble);
    }
}
