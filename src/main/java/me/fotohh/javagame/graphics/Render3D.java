package me.fotohh.javagame.graphics;

import me.fotohh.javagame.display.Game;

public class Render3D extends Render {

    public double[] zBuffer;
    private static final double RENDER_DISTANCE = 10000;

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

        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;

            double z = floorPos / ceiling;

            if (ceiling < 0) {
                z = ceilingPos / -ceiling;
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
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
    }

    public void renderDistanceLimiter() {
        for (int i = 0; i < width * height; i++) {
            int color = pixels[i];
            int brightness = (int) (RENDER_DISTANCE / zBuffer[i]);

            if (brightness < 0) {
                brightness = 0;
            }
            if (brightness > 255) {
                brightness = 255;
            }

            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = (color) & 0xff;
            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}
