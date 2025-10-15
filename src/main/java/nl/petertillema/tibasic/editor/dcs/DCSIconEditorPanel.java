package nl.petertillema.tibasic.editor.dcs;

import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import static nl.petertillema.tibasic.TIBasicPaletteColors.TIBASIC_PALETTE;

/**
 * Lightweight 16x16 icon editor panel for DCS icons.
 * Each pixel is stored as a single hex character (0–F) referencing the TI palette index.
 */
public class DCSIconEditorPanel extends JPanel {
    private static final int GRID = 16;
    private static final int CELL = 16;
    private static final int GAP = 1;

    private final char[] data = new char[GRID * GRID];

    private boolean dragging = false;
    private int paintIndex = 1;

    public DCSIconEditorPanel(String iconData) {
        // Initialize data from the input
        Arrays.fill(data, '0');
        if (iconData != null && !iconData.isEmpty()) {
            int len = Math.min(iconData.length(), data.length);
            for (int i = 0; i < len; i++) {
                char ch = Character.toUpperCase(iconData.charAt(i));
                if (Character.digit(ch, 16) >= 0) data[i] = ch;
            }
        }

        setOpaque(true);
        setBackground(JBColor.PanelBackground);
        setPreferredSize(new Dimension(GRID * (CELL + GAP) + GAP, GRID * (CELL + GAP) + GAP));

        MouseAdapter mouse = new MouseAdapter() {
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
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public String getIconData() {
        return new String(data);
    }

    public void setSelectedColorIndex(int index) {
        if (index < 0) index = 0;
        paintIndex = index & 0xF;
        repaint();
    }

    public int getSelectedColorIndex() {
        return paintIndex & 0xF;
    }

    private void paintAt(int x, int y) {
        int idx = cellIndexAt(x, y);
        if (idx < 0) return;
        char hex = toHex(paintIndex);
        if (data[idx] != hex) {
            data[idx] = hex;
            repaint(cellRect(idx));
        }
    }

    private static char toHex(int v) {
        return Integer.toHexString(v & 0xF).toUpperCase().charAt(0);
    }

    private int cellIndexAt(int x, int y) {
        // account for outer gap
        int gx = x - GAP;
        int gy = y - GAP;
        if (gx < 0 || gy < 0) return -1;
        int stride = CELL + GAP;
        int cx = gx / stride;
        int cy = gy / stride;
        if (cx >= GRID || cy >= GRID) return -1;
        int rx = gx % stride;
        int ry = gy % stride;
        if (rx == CELL || ry == CELL) return -1; // in the gap line
        return cy * GRID + cx;
    }

    private Rectangle cellRect(int idx) {
        int cx = idx % GRID;
        int cy = idx / GRID;
        int stride = CELL + GAP;
        int x = GAP + cx * stride;
        int y = GAP + cy * stride;
        return new Rectangle(x, y, CELL, CELL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // background
        g2.setColor(JBColor.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // draw cells
        int stride = CELL + GAP;
        for (int i = 0; i < data.length; i++) {
            int cx = i % GRID;
            int cy = i / GRID;
            int px = GAP + cx * stride;
            int py = GAP + cy * stride;
            int pi = Character.digit(data[i], 16);
            if (pi < 0 || pi >= 16) pi = 0;
            if (pi == 0) {
                // Transparent pixel: draw checkerboard so it isn't confused with white
                int tile = 8;
                Color light = new JBColor(Gray._240, Gray._70);
                Color dark  = new JBColor(Gray._220, Gray._90);
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
                g2.setColor(TIBASIC_PALETTE.get(pi));
                g2.fillRect(px, py, CELL, CELL);
            }
        }

        // grid lines
        g2.setColor(JBColor.border());
        for (int i = 0; i <= GRID; i++) {
            int p = GAP + i * stride - GAP / 2;
            // vertical
            g2.drawLine(GAP + i * stride - GAP, 0, GAP + i * stride - GAP, GRID * stride + GAP);
            // horizontal
            g2.drawLine(0, GAP + i * stride - GAP, GRID * stride + GAP, GAP + i * stride - GAP);
        }

    }
}