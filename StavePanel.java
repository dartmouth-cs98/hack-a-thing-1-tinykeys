import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StavePanel extends JPanel {

    // stores shapes for drawing the currently played notes
    List<Ellipse2D.Float> elipses = new ArrayList<Ellipse2D.Float>();

    // returns the dimensions for the panel with the staff and notes
    public Dimension getPreferredSize(){
        return new Dimension(395, 125);
    }

    // draws the panel
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw staff for notes to be displayed upon
        g.setColor(Color.BLACK);
        g.fillRect(20, 20, 335, 3);
        g.fillRect(20, 40, 335, 3);
        g.fillRect(20, 60, 335, 3);
        g.fillRect(20, 80, 335, 3);
        g.fillRect(20, 100, 335, 3);

        g.drawString("Attack(ms)",275,120);
        g.drawString("Decay(ms)", 335, 120);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        // draw notes
        for(Ellipse2D.Float note : elipses){
            g2.fill(note);
        }
    }

    // adds the required notes to ellipses and then calls repaint()
    public void drawNotes(Integer[] noteCodes){
        elipses = new ArrayList();

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_A))) {
            // middle c is being played
            elipses.add(new Ellipse2D.Float(20, 115, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_W))) {
            // c#
            elipses.add(new Ellipse2D.Float(30, 110, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_S))) {
            // d
            elipses.add(new Ellipse2D.Float(40, 105, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_E))) {
            // d#
            elipses.add(new Ellipse2D.Float(50, 100, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_D))) {
            // e
            elipses.add(new Ellipse2D.Float(60, 95, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_F))) {
            //f
            elipses.add(new Ellipse2D.Float(80, 85, 10, 10));

        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_T))) {
            // f#
            elipses.add(new Ellipse2D.Float(90, 80, 10, 10));
        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_G))) {
            // g
            elipses.add(new Ellipse2D.Float(100, 75, 10, 10));

        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_Y))) {
            // g#
            elipses.add(new Ellipse2D.Float(110, 70, 10, 10));

        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_H))) {
            // a
            elipses.add(new Ellipse2D.Float(120, 65, 10, 10));

        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_U))) {
            // a#
            elipses.add(new Ellipse2D.Float(130, 60, 10, 10));

        }

        if (Arrays.asList(noteCodes).contains(Integer.valueOf(KeyEvent.VK_J))) {
            // b
            elipses.add(new Ellipse2D.Float(140, 55, 10, 10));

        }

        repaint();
    }
}
