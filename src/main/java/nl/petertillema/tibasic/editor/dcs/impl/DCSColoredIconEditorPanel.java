package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSIconEditorPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_PALETTE;

public class DCSColoredIconEditorPanel extends AbstractDCSIconEditorPanel {

    private final char[] data = new char[256];

    public DCSColoredIconEditorPanel(String iconData) {
        super(true);

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
        return 16;
    }

    public String getIconData() {
        return new String(data);
    }

    @Override
    protected List<Color> getPalette() {
        return TIBASIC_PALETTE;
    }

    @Override
    protected int getData(int index) {
        return Character.digit(this.data[index], 16);
    }

    @Override
    protected void setData(int index, int paletteIndex) {
        this.data[index] = toHex(paletteIndex);
    }
}