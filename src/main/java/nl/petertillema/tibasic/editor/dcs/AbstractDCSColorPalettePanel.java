package nl.petertillema.tibasic.editor.dcs;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class AbstractDCSColorPalettePanel extends JPanel {

    private static final int CELL = 14;
    private static final int GAP = 3;

    private final boolean firstIndexIsTransparent;
    private int selected = 1;

    public interface SelectionListener {
        void onSelected(int colorIndex);
    }

    private SelectionListener selectionListener;

    public AbstractDCSColorPalettePanel(boolean firstIndexIsTransparent) {
        this.firstIndexIsTransparent = firstIndexIsTransparent;

        setOpaque(true);
        setBackground(JBColor.PanelBackground);
        setPreferredSize(new Dimension(this.getPalette().size() * (CELL + GAP) + GAP, CELL + 2 * GAP + 2));

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int idx = indexAt(e.getX(), e.getY());
                setSelectedIndex(idx);
                if (selectionListener != null) selectionListener.onSelected(idx);
            }
        };
        addMouseListener(mouse);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // background
        g2.setColor(JBColor.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        List<Color> palette = this.getPalette();

        int stride = CELL + GAP;
        for (int i = 0; i < palette.size(); i++) {
            int px = GAP + i * stride;
            int py = GAP;
            if (i == 0 && this.firstIndexIsTransparent) {
                // Transparent swatch: draw checkerboard instead of plain fill
                int tile = 2;
                Color light = new JBColor(new Color(240, 240, 240), new Color(70, 70, 70));
                Color dark = new JBColor(new Color(220, 220, 220), new Color(90, 90, 90));
                for (int yy = 0; yy < CELL; yy += tile) {
                    for (int xx = 0; xx < CELL; xx += tile) {
                        boolean isLight = (((xx / tile) + (yy / tile)) & 1) == 0;
                        g2.setColor(isLight ? light : dark);
                        int w = Math.min(tile, CELL - xx);
                        int h = Math.min(tile, CELL - yy);
                        g2.fillRect(px + xx, py + yy, w, h);
                    }
                }
            } else {
                g2.setColor(palette.get(i));
                g2.fillRect(px, py, CELL, CELL);
            }
            // border
            g2.setColor(JBColor.border());
            g2.drawRect(px, py, CELL, CELL);
            // selection highlight (more explicit)
            if (i == selected) {
                // Outer thick border
                g2.setColor(new JBColor(new Color(0, 0, 0), new Color(255, 255, 255)));
                g2.drawRect(px - 2, py - 2, CELL + 4, CELL + 4);
                g2.drawRect(px - 1, py - 1, CELL + 2, CELL + 2);
            }
        }
    }

    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }

    public void setSelectedIndex(int idx) {
        if (selected != idx) {
            selected = idx;
            repaint();
        }
    }

    protected abstract List<Color> getPalette();

    private int indexAt(int x, int y) {
        int gx = x - GAP;
        if (gx < 0) return -1;
        int stride = CELL + GAP;
        int cx = gx / stride;
        int rx = gx % stride;
        if (rx >= CELL) return -1; // in the gap
        if (cx >= getPalette().size()) return -1;
        return cx;
    }

}
