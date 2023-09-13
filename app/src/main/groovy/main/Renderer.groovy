package main

import javax.swing.*
import java.awt.*
import java.awt.event.*
import java.awt.image.*

class BoardDrawer extends JPanel implements ActionListener {
    private BufferedImage image;
    private Timer timer;
    private int scale;

    Board board;
    Rule[] rules;

    BoardDrawer(int width, int height, int scale, Board board, Rule[] rules) {
        this.scale = scale;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        this.board = board;
        this.rules = rules;
        this.timer = new Timer(0, this)

        this.timer.start();
    }
  
    void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null)
    }

    void actionPerformed(ActionEvent e) {
        if(!this.board.hasSteps()){
            this.timer.stop()
            return;
        }

        def tile = this.board.step(this.rules)
        def color = Color.WHITE

        if(tile.type() == TileType.SAND) {
            color = Color.YELLOW
        } else if(tile.type() == TileType.WATER) {
            color = Color.BLUE
        } else if(tile.type() == TileType.FOREST) {
            color = Color.GREEN
        } else if(tile.type() == TileType.STONE) {
            color = Color.DARK_GRAY
        } else if(tile.type() == TileType.MOUNTAIN_BOTTOM) {
            color = Color.GRAY
        }else if(tile.type() == TileType.MOUNTAIN_TOP) {
            color = Color.WHITE
        }
        
        def g2d = (Graphics2D)image.getGraphics();
		g2d.setColor( color );

        def rectangle = new Rectangle()
        rectangle.setBounds(tile.y * this.scale, tile.x * this.scale, scale, scale)

		g2d.fill( rectangle );
        repaint();
    }
}

class Renderer extends JFrame {
    int width;
    int height;
    int scale;

    Board board;
    Rule[] rules;

    Renderer(int width, int height, int scale, Board board, Rule[] rules) {
        this.width = width
        this.height = height
        this.scale = scale
        this.board=board;
        this.rules=rules
    }

    void setUpGui() {
        setTitle("Map")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setSize(this.width, this.height);
        add(new BoardDrawer(this.width, this.height, this.scale, this.board, this.rules));
        setVisible(true);
    }
}