package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    int FPS = 60;

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    TileManager tileM = new TileManager(this);
    public Player player = new Player(this, keyH);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Sound sound = new Sound();
    public Sound soundEffect = new Sound();
    public UI ui = new UI(this);

    // Entity and Object
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);

    // GAME STATE
//    public int gameState;
//    public final int playState = 1;
//    public final int pauseState = 2;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000/FPS;
        double deltaTime = 0;

        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime)/drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;


            if(deltaTime >= 1) {
                update();
                repaint();
                deltaTime--;
                drawCount++;
            }

            if(timer >= 1000000000) {
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void setupGame() {
        aSetter.setObject();
        play(0);
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);

        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        player.draw(g2);

        ui.draw(g2);

        g2.dispose();
    }

    public void play(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

    public void stopMusic() {
        sound.stop();
    }
}
