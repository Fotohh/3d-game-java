package me.fotohh.javagame.graphics;

import me.fotohh.javagame.display.Game;
import me.fotohh.javagame.input.Controller;

public class Render3D extends Render {

    public double[] zBuffer;
    private final Game game;

    private static final double RENDER_DISTANCE = 10000;
    private final static double BOBBING = 0.4, BOB_SPEED = 6.0;

    private static final double FLOOR_POSITION = 8.0, CEILING_POSITION = 800.0;
    private double forward, right, rotation, cosine, sine, up, walking;


    public Render3D(int width, int height, Game game) {
        super(width, height);
        this.game = game;
        zBuffer = new double[width * height];
    }

    public void floor() {
        double forward = game.controls.z;
        double right = game.controls.x;
        double rotation = game.controls.rotation;
        double cosine = Math.cos(rotation);
        double sine = Math.sin(rotation);
        double up = game.controls.y;
        double walking = 0;

        if (Controller.moving) {
            double bobSpeed = BOBBING;
            double bobbing = BOB_SPEED;

            if (Controller.sprinting) {
                bobSpeed *= 0.9;
            } else if (Controller.crouching) {
                bobSpeed *= 1.3;
                bobbing *= 0.8;
            }

            walking = Math.sin(game.time / bobSpeed) * bobbing;
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        for (int y = 0; y < height; y++) {
            double ceiling = (double) (y - halfHeight) / height;
            double z = ceiling < 0 ? (CEILING_POSITION - up + walking) / -ceiling : (FLOOR_POSITION + up - walking) / ceiling;

            for (int x = 0; x < width; x++) {
                double depth = (double) (x - halfWidth) / height * z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;

                if (z < 400) {
                    pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
                }
            }
        }
    }

    public void renderWall(double xLeft, double xRight, double zDistance, double yHeight) {
       // double xcLeft = ((xLeft) - right) * 2;
    }

    public void renderDistanceLimiter() {
        for (int i = 0; i < width * height; i++) {
            int color = pixels[i];
            int brightness = (int) (RENDER_DISTANCE / zBuffer[i]);
            brightness = Math.max(0, Math.min(255, brightness));

            int r = ((color >> 16) & 0xff) * brightness / 255;
            int g = ((color >> 8) & 0xff) * brightness / 255;
            int b = (color & 0xff) * brightness / 255;

            pixels[i] = (r << 16) | (g << 8) | b;
        }
    }
}
