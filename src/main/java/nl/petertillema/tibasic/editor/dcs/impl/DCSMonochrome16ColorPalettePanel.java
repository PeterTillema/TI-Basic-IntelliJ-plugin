package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSColorPalettePanel;

import java.awt.*;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.MONOCHROME_PALETTE;

public final class DCSMonochrome16ColorPalettePanel extends AbstractDCSColorPalettePanel {

    public DCSMonochrome16ColorPalettePanel() {
        super(false);
    }

    @Override
    protected List<Color> getPalette() {
        return MONOCHROME_PALETTE;
    }
}
