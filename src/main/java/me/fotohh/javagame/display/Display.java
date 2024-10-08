package me.fotohh.javagame.display;

import me.fotohh.javagame.graphics.Render;
import me.fotohh.javagame.graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.fotohh.javagame.log.Logger.*;

public class Display extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String title = "3D Game";
    private final Screen screen;

    private Thread thread;
    private final BufferedImage img;
    private boolean runnning = false;
    private final int[] pixels;

    public Display(){
        screen = new Screen(WIDTH, HEIGHT);
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        JFrame window = new JFrame(title);
        window.add(this);
        window.pack();
        window.setTitle(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);

        start();
    }

    private void start(){
        if(runnning) return;
        runnning = true;
        thread = new Thread(this);
        thread.start();

    }

    private void stop() {
        if(!runnning) return;
        runnning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {

            severe("Encountered an error stopping the game!");
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (runnning){

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(runnning)
                render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                info("FPS: "+frames );
                frames = 0;
            }


        }
        stop();

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render();

        for(int i = 0; i < WIDTH*HEIGHT; i++){
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

        g.dispose();
        bs.show();
    }

    private void tick() {



    }

}
