package nl.petertillema.tibasic.editor.dcs;

import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBCachingScalableIcon;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_PALETTE;

public class DCSIcon extends JBCachingScalableIcon<DCSIcon> {

    private static final int SIZE = 16;
    private final String iconData;

    public DCSIcon(String data) {
        this.iconData = data == null ? "" : data;
    }

    private DCSIcon(DCSIcon icon) {
        super(icon);
        this.iconData = icon.iconData;
    }

    @Override
    public void paintIcon(Component component, Graphics g, int x, int y) {
        GraphicsConfig cfg = GraphicsUtil.setupAAPainting(g);
        Graphics2D g2 = (Graphics2D) g;
        // Crisp pixel art
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int maxPixels = SIZE * SIZE;
        int len = iconData.length();
        int n = Math.min(maxPixels, len);
        for (int i = 0; i < n; i++) {
            char ch = iconData.charAt(i);
            int idx = Character.digit(ch, 16);
            if (idx < 0 || idx >= 16) idx = 0;
            if (idx == 0) continue; // transparent pixel, leave background as-is
            int px = i % SIZE;
            int py = i / SIZE;
            g2.setColor(TIBASIC_PALETTE.get(idx));
            g2.fillRect(x + px, y + py, 1, 1);
        }

        cfg.restore();
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }

    @Override
    public @NotNull DCSIcon copy() {
        return new DCSIcon(this);
    }
}
