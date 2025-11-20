package nl.petertillema.tibasic.editor.dcs;

import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class AbstractDCSIconEditorPanel extends JPanel {

    private static final int CELL_WIDTH = 16;
    private static final int CELL_HEIGHT = 16;
    private static final int GAP = 1;

    private boolean dragging = false;
    private int paintIndex = 1;
    private final boolean firstIndexIsTransparent;

    protected AbstractDCSIconEditorPanel(boolean firstIndexIsTransparent) {
        this.firstIndexIsTransparent = firstIndexIsTransparent;

        setOpaque(true);
        setBackground(JBColor.PanelBackground);
        setPreferredSize(new Dimension(this.getGridSize() * (CELL_WIDTH + GAP) + GAP, this.getGridSize() * (CELL_HEIGHT + GAP) + GAP));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragging = true;
                paintAt(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    paintAt(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // background
        g2.setColor(JBColor.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // draw cells
        int stride = CELL_WIDTH + GAP;
        for (int i = 0; i < this.getGridSize() * this.getGridSize(); i++) {
            int cx = i % this.getGridSize();
            int cy = i / this.getGridSize();
            int px = GAP + cx * stride;
            int py = GAP + cy * stride;
            int pi = this.getData(i);
            if (pi == 0 && this.firstIndexIsTransparent) {
                // Transparent pixel: draw checkerboard so it isn't confused with white
                int tile = 8;
                Color light = new JBColor(Gray._240, Gray._70);
                Color dark = new JBColor(Gray._220, Gray._90);
                for (int yy = 0; yy < CELL_HEIGHT; yy += tile) {
                    for (int xx = 0; xx < CELL_WIDTH; xx += tile) {
                        boolean isLight = (((xx / tile) + (yy / tile)) & 1) == 0;
                        g2.setColor(isLight ? light : dark);
                        int w = Math.min(tile, CELL_WIDTH - xx);
                        int h = Math.min(tile, CELL_HEIGHT - yy);
                        g2.fillRect(px + xx, py + yy, w, h);
                    }
                }
            } else {
                g2.setColor(this.getPalette().get(pi));
                g2.fillRect(px, py, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        // grid lines
        g2.setColor(JBColor.border());
        for (int i = 0; i <= this.getGridSize(); i++) {
            // vertical
            g2.drawLine(GAP + i * stride - GAP, 0, GAP + i * stride - GAP, this.getGridSize() * stride + GAP);
            // horizontal
            g2.drawLine(0, GAP + i * stride - GAP, this.getGridSize() * stride + GAP, GAP + i * stride - GAP);
        }
    }

    public void setSelectedColorIndex(int index) {
        paintIndex = index;
        repaint();
    }

    public int getSelectedColorIndex() {
        return paintIndex;
    }

    public static char toHex(int v) {
        return Integer.toHexString(v & 0xF).toUpperCase().charAt(0);
    }

    protected abstract int getGridSize();

    protected abstract String getIconData();

    protected abstract List<Color> getPalette();

    protected abstract int getData(int index);

    protected abstract void setData(int index, int paletteIndex);

    private void paintAt(int x, int y) {
        int idx = cellIndexAt(x, y);
        if (idx < 0) return;
        this.setData(idx, paintIndex);
        repaint(cellRect(idx));
    }

    private int cellIndexAt(int x, int y) {
        // account for outer gap
        int gx = x - GAP;
        int gy = y - GAP;
        if (gx < 0 || gy < 0) return -1;
        int stride = CELL_WIDTH + GAP;
        int cx = gx / stride;
        int cy = gy / stride;
        if (cx >= this.getGridSize() || cy >= this.getGridSize()) return -1;
        int rx = gx % stride;
        int ry = gy % stride;
        if (rx == CELL_WIDTH || ry == CELL_WIDTH) return -1; // in the gap line
        return cy * this.getGridSize() + cx;
    }

    private Rectangle cellRect(int idx) {
        int cx = idx % this.getGridSize();
        int cy = idx / this.getGridSize();
        int stride = CELL_WIDTH + GAP;
        int x = GAP + cx * stride;
        int y = GAP + cy * stride;
        return new Rectangle(x, y, CELL_WIDTH, CELL_HEIGHT);
    }

}
