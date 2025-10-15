package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSColorPalettePanel;

import java.awt.*;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.MONOCHROME_PALETTE;

public class DCSMonochrome8ColorPalettePanel extends AbstractDCSColorPalettePanel {

    public DCSMonochrome8ColorPalettePanel() {
        super(false);
    }

    @Override
    protected List<Color> getPalette() {
        return MONOCHROME_PALETTE;
    }
}
