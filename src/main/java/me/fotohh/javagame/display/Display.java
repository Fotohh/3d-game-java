package me.fotohh.javagame.display;

import me.fotohh.javagame.graphics.Screen;
import me.fotohh.javagame.input.InputHandler;
import org.fusesource.jansi.AnsiConsole;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;

import static me.fotohh.javagame.log.Logger.*;

public class Display extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String title = "3D Game";

    private final Screen screen;
    private Thread thread;
    private final Game game;
    private final BufferedImage img;
    private boolean runnning = false;
    private final int[] pixels;
    private final InputHandler input;

    public Display(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
        addFocusListener(input);

        JFrame window = new JFrame(title);
        window.add(this);
        window.pack();
        window.setTitle(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
            onDisable();
            e.printStackTrace();
            System.exit(0);
        }
        onDisable();
    }

    public void onDisable(){
        AnsiConsole.systemUninstall();
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = (double) 1 /60;
        int tickCount = 0;
        boolean ticked = false;

        while (runnning){

            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;
            while(unprocessedSeconds > secondsPerTick){
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if(tickCount % 60 == 0){
                    info("FPS: " + frames);
                    previousTime += 1000;
                    frames = 0;
                }
            }
            if(ticked){
                render();
                frames++;
            }
            render();


        }
        stop();

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for(int i = 0; i < WIDTH*HEIGHT; i++){
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

        g.dispose();
        bs.show();
    }

    private void tick() {

        game.tick(input.key);

    }

}
