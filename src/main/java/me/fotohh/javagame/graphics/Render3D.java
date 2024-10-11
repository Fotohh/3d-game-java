package me.fotohh.javagame.graphics;

import me.fotohh.javagame.display.Game;
import me.fotohh.javagame.input.Controller;

import java.util.Random;

public class Render3D extends Render {

    public double[] zBuffer;
    private static final double RENDER_DISTANCE = 10000;
    private Random random = new Random();

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void floor(Game game) {
        double floorPos = 8.0;
        double ceilingPos = 800;
        double forward = game.controls.z;
        double right = game.controls.x;
        double rotation = game.controls.rotation;
        double cosine = Math.cos(rotation);
        double sine = Math.sin(rotation);
        double up = game.controls.y;
        double walking = 0;

        if (Controller.moving) {
            double bobSpeed = Controller.bobSpeed;
            double bobbing = Controller.bobbing;

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
            double z = ceiling < 0 ? (ceilingPos - up + walking) / -ceiling : (floorPos + up - walking) / ceiling;

            for (int x = 0; x < width; x++) {
                double depth = (double) (x - halfWidth) / height * z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x+y*width] = z;

                if (z < 400) {
                    pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
                }
            }
        }
        for(int i = 0; i < 10000; i++) {
            double xx = random.nextDouble();
            double yy = random.nextDouble();
            double zz = random.nextDouble();

            int xPixel = (int) (xx / zz * height + width);
            int yPixel = (int) (yy / zz * height + height);
            if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
                pixels[xPixel + yPixel * width] = 0xff00ff;
            }
        }
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
