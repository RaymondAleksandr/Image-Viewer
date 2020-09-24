import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class HomeWork3 extends JFrame implements ActionListener {
    BufferedImage bimage1, bimage2, getImage;
    JButton button1, button2, button3, button4;
    String filename = "imageA.jpg";

    int xCenter, yCenter;
    int[][] rect = {{0, 0}, {0, 0}};
    double scale = 1.0;
    double rotate = 0.0;
    double rate;

    public HomeWork3() {
        setTitle("Painter");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyListener myListener = new MyListener();
        addMouseListener(myListener);
        addMouseMotionListener(myListener);

        button1 = new JButton("Zoom in");
        button1.addActionListener(this);
        button1.setActionCommand("zoom in");

        button2 = new JButton("Zoom out");
        button2.addActionListener(this);
        button2.setActionCommand("zoom out");

        button3 = new JButton("Left Rotation");
        button3.addActionListener(this);
        button3.setActionCommand("left rotation");

        button4 = new JButton("Right Rotation");
        button4.addActionListener(this);
        button4.setActionCommand("right rotation");

        JPanel p = new JPanel();
        p.add(button1);
        p.add(button2);
        p.add(button3);
        p.add(button4);
        getContentPane().add(p, BorderLayout.NORTH);

        setVisible(true);
        initialization();
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        int hposition = 550;
        int size = 5;

        g.clearRect(0, 60, 700, 800);
        if (bimage1 != null) {
            g.drawImage(bimage1, 30, 60, this);
        }

        if (bimage2 != null) {
            g.drawImage(bimage2, 30, 60, this);
        }

        if (getImage != null) {
            if (getImage.getHeight() > 240) {
                rate = 240 / getImage.getHeight();
            } else {
                rate = 1.0;
            }
            g.drawImage(getImage, 30, hposition, (int) (getImage.getWidth() * rate), (int) (getImage.getHeight() * rate), this);
        }

        g.setColor(Color.red);
        g.drawRect(rect[0][0], rect[0][1], rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
        g.setColor(Color.yellow);
        g.fillRect((rect[1][0] + rect[0][0]) / 2 - size / 2, (rect[1][1] + rect[0][1]) / 2 - size / 2, size, size);

    }

    public void initialization() {
        try {
            File file = new File(filename);
            bimage1 = ImageIO.read(file);
            xCenter = bimage1.getWidth() / 2;
            yCenter = bimage1.getHeight() / 2;
        } catch (IOException e) {
        }
        repaint();
    }

    void transform() {
        bimage2 = new BufferedImage(bimage1.getWidth(), bimage1.getHeight(), bimage1.getType());
        AffineTransform affine = new AffineTransform();
        affine.translate(xCenter, yCenter);
        affine.scale(scale, scale);
        affine.rotate(rotate);
        affine.translate(-xCenter, -yCenter);
        AffineTransformOp operator = new AffineTransformOp(affine, AffineTransformOp.TYPE_BICUBIC);
        operator.filter(bimage1, bimage2);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("zoom in")) {
            scale += 0.1;
            transform();
            getImage = bimage2.getSubimage(rect[0][0] - 30, rect[0][1] - 60, rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
            repaint();
        }

        if (cmd.equals("zoom out")) {
            scale -= 0.1;
            transform();
            getImage = bimage2.getSubimage(rect[0][0] - 30, rect[0][1] - 60, rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
            repaint();
        }

        if (cmd.equals("left rotation")) {
            rotate -= Math.toRadians(1.0);
            transform();
            getImage = bimage2.getSubimage(rect[0][0] - 30, rect[0][1] - 60, rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
            repaint();
        }

        if (cmd.equals("right rotation")) {
            rotate += Math.toRadians(1.0);
            transform();
            getImage = bimage2.getSubimage(rect[0][0] - 30, rect[0][1] - 60, rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
            repaint();
        }
    }

    public static void main(String[] args) {
        new HomeWork3();
    }

    class MyListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            rect[0][0] = e.getX();
            rect[0][1] = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
            getImage = bimage1.getSubimage(rect[0][0] - 30, rect[0][1] - 60, rect[1][0] - rect[0][0], rect[1][1] - rect[0][1]);
            repaint();
        }

        public void mouseDragged(MouseEvent e) {
            rect[1][0] = e.getX();
            rect[1][1] = e.getY();
            repaint();
        }
    }
}
