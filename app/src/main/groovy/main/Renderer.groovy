package main

import javax.swing.*
import javax.imageio.ImageIO;
import java.awt.*
import java.awt.event.*
import java.awt.image.*

import java.util.HashMap;
import java.util.Map;

interface TextureFactory {
    public BufferedImage get(TileType t)
}

class PipeFactory implements TextureFactory {
    Map<String, BufferedImage> pipetextures;
    String [] textureFileNames = ["pipelr","pipetb", "pipecross"]
    PipeFactory(int width, int height){
        this.pipetextures = new HashMap<>();

        textureFileNames.stream().map( fileName -> {
            BufferedImage texture = ImageIO.read("app/src/main/resources/" + fileName + ".png");
            this.pipetextures.put(fileName, texture)
        })

        this.pipetextures.put("nothing", new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    public BufferedImage get(TileType t) {
        switch (t) {
            case TileType.LR: 
                this.pipetextures.get("pipelr");
                break;
            case TileType.TB: 
                this.pipetextures.get("pipetb");
                break;
            case TileType.CROSS: 
                this.pipetextures.get("pipecross");
                break;
            case TileType.NOTHING: 
                this.pipetextures.get("nothing");
                break;
            default: 
                throw new Exception("No matching tile type");
                break;
        }
    }
}

abstract class MapDrawer extends JPanel {
    int width;
    int height;
    int scale;
    
    protected Generator gen;
    protected TextureFactory f;

    MapDrawer(int width, int height, int scale, Generator gen, TextureFactory f){
        this.width = width
        this.height = height
        this.scale = scale
        this.gen = gen
        this.f = f
    }  
}

abstract class BufferedDrawer extends MapDrawer {
    private BufferedImage image;

    BufferedDrawer(int width, int height, int scale, Generator gen, TextureFactory f) {
       super(width, height,scale, gen, f)

       this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    }

    void paintToBuffer(Tile tile) {
        def texture = this.f.get(tile.type())
        
        def g2d = (Graphics2D)image.getGraphics();
        g2d.drawImage(texture, tile.x, tile.y, null)
    }

    void drawBuffer(Graphics g) {
        g.drawImage(image, 0, 0, null)
    }
}

class Finished extends BufferedDrawer {
    
    Finished(int width, int height, int scale, Generator gen, TextureFactory f) {
       super(width, height,scale, gen, f)
    }

    void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        while(this.gen.hasSteps()) {
            paintToBuffer(this.gen.step())
        }

        drawBuffer(g)
    }
}

class StepWise extends BufferedDrawer implements ActionListener {
    private Timer timer;

    StepWise(int width, int height, int scale, Generator gen, TextureFactory f) {
        super(width, height,scale, gen, f)

        this.timer = new Timer(0, this)

        this.timer.start();
    }
  
    void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBuffer(g)
    }

    void actionPerformed(ActionEvent e) {
        if(!this.gen.hasSteps()){
            this.timer.stop()
            return;
        }

        def tile = this.gen.step()
        paintToBuffer(tile)
        repaint();
    }
}

class Renderer extends JFrame {
    int width;
    int height;
    MapDrawer drawer

    Renderer(MapDrawer drawer) {
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