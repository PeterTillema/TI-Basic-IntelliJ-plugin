package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSColorPalettePanel;

import java.awt.*;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_PALETTE;

public class DCSColoredColorPalettePanel extends AbstractDCSColorPalettePanel {

    public DCSColoredColorPalettePanel() {
        super(true);
    }

    @Override
    protected List<Color> getPalette() {
        return TIBASIC_PALETTE;
    }
}
