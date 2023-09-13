package main

import javax.swing.*
import java.awt.*
import java.awt.event.*
import java.awt.image.*

abstract class BoardDrawer extends JPanel {
    int width;
    int height;
    int scale;
    
    protected Board board;
    protected Rule[] rules;

    BoardDrawer(int width, int height, int scale, Board board, Rule[] rules){
        this.width = width
        this.height = height
        this.scale = scale
        this.board = board
        this.rules = rules
    }

    Color getColor(TileType type) {
        if(type == TileType.SAND) {
            return Color.YELLOW
        } 
        if(type == TileType.WATER) {
            return Color.BLUE
        } 
        if(type == TileType.FOREST) {
           return Color.GREEN
        } 
        if(type == TileType.STONE) {
           return Color.DARK_GRAY
        } 
        if(type == TileType.MOUNTAIN_BOTTOM) {
           return Color.GRAY
        }
        if(type == TileType.MOUNTAIN_TOP) {
           return Color.WHITE
        }
        if(type == TileType.NOTHING) {
           return Color.RED
        }
    }
}

abstract class BufferedDrawer extends BoardDrawer {
    private BufferedImage image;

    BufferedDrawer(int width, int height, int scale, Board board, Rule[] rules) {
       super(width, height,scale, board,rules)

       this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    }

    void paintToBuffer(Tile tile) {
        def color = getColor(tile.type())
        
        def g2d = (Graphics2D)image.getGraphics();
		g2d.setColor( color );

        def rectangle = new Rectangle()
        rectangle.setBounds(tile.y * this.scale, tile.x * this.scale, scale, scale)

		g2d.fill( rectangle );
    }

    void drawBuffer(Graphics g) {
        g.drawImage(image, 0, 0, null)
    }
}

class Finished extends BufferedDrawer {
    
    Finished(int width, int height, int scale, Board board, Rule[] rules) {
       super(width, height,scale, board,rules)
    }

    void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        while(board.hasSteps()) {
            paintToBuffer(board.step(this.rules))
        }

        drawBuffer(g)
    }
}

class StepWise extends BufferedDrawer implements ActionListener {
    private Timer timer;

    StepWise(int width, int height, int scale, Board board, Rule[] rules) {
        super(width, height,scale, board,rules)

        this.timer = new Timer(0, this)

        this.timer.start();
    }
  
    void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBuffer(g)
    }

    void actionPerformed(ActionEvent e) {
        if(!this.board.hasSteps()){
            this.timer.stop()
            return;
        }

        def tile = this.board.step(this.rules)
        paintToBuffer(tile)
        repaint();
    }
}

class Renderer extends JFrame {
    int width;
    int height;
    BoardDrawer drawer

    Renderer(BoardDrawer drawer) {
        this.width = drawer.width + 10
        this.height = drawer.height + 50
        this.drawer = drawer
    }

    void setUpGui() {
        setTitle("Map")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setSize(this.width, this.height);
        add(drawer);
        setVisible(true);
    }
}