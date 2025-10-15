package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSIcon;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_PALETTE;

public class DCSColoredIcon extends AbstractDCSIcon {

    private final String iconData;

    public DCSColoredIcon(String data) {
        this.iconData = data == null ? "" : data;
    }

    private DCSColoredIcon(DCSColoredIcon icon) {
        this.iconData = icon.iconData;
    }

    @Override
    protected int getSize() {
        return 16;
    }

    @Override
    protected int getData(int index) {
        return this.iconData.length() <= index ? 0 : Character.digit(this.iconData.charAt(index), 16);
    }

    @Override
    protected List<Color> getPalette() {
        return TIBASIC_PALETTE;
    }

    @Override
    public @NotNull DCSColoredIcon copy() {
        return new DCSColoredIcon(this);
    }
}
