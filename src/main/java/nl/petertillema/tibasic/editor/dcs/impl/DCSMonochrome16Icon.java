package nl.petertillema.tibasic.editor.dcs.impl;

import nl.petertillema.tibasic.editor.dcs.AbstractDCSIcon;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static nl.petertillema.tibasic.TIBasicPaletteColors.MONOCHROME_PALETTE;

public class DCSMonochrome16Icon extends AbstractDCSIcon {

    private final String iconData;

    public DCSMonochrome16Icon(String data) {
        this.iconData = data == null ? "" : data;
    }

    private DCSMonochrome16Icon(DCSMonochrome16Icon icon) {
        this.iconData = icon.iconData;
    }

    @Override
    protected int getSize() {
        return 16;
    }

    @Override
    protected int getData(int index) {
        if (this.iconData.length() < index / 4) return 0;
        var nibble = Character.digit(this.iconData.charAt(index / 4), 16);
        var mask = 1 << (index % 4);
        return (nibble & mask) == 0 ? 0 : 1;
    }

    @Override
    protected List<Color> getPalette() {
        return MONOCHROME_PALETTE;
    }

    @Override
    public @NotNull DCSMonochrome16Icon copy() {
        return new DCSMonochrome16Icon(this);
    }
}
