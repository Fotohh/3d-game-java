package me.fotohh.javagame.graphics;

import me.fotohh.javagame.display.Game;
import me.fotohh.javagame.input.Controller;

public class Render3D extends Render {

    public double[] zBuffer;

    private final Game game;

    private static final double
            RENDER_DISTANCE = 10000,
            BOBBING = 0.5,
            BOB_SPEED = 6.0,
            FLOOR_POSITION = 8.0,
            CEILING_POSITION = 800.0;

    private double forward;
    private double right;
    private double cosine;
    private double sine;
    private double up;

    public Render3D(int width, int height, Game game) {
        super(width, height);
        this.game = game;
        zBuffer = new double[width * height];
    }

    public void floor() {
        forward = game.controls.z;
        right = game.controls.x;
        double rotation = game.controls.rotation;
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);
        up = game.controls.y;
        double walking = 0;

        if (Controller.moving) {
            double bobSpeed = BOB_SPEED;
            double bobbing = BOBBING;

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

    public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {

        double upCorrect = 0.0625;
        double rightCorrect = 0.0625;
        double leftCorrect = 0.0625;

        double xcLeft = ((xLeft) - (right)) * 2;
        double zcLeft = ((zDistanceLeft) - (forward)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTL = ((-yHeight) - (-up * upCorrect)) * 2;
        double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect)) * 2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = ((xRight) - right) * 2;
        double zcRight = ((zDistanceRight) - forward) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTR = ((-yHeight) - (-up * upCorrect)) * 2;
        double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect)) * 2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2.0);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2.0);

        if (xPixelLeft >= xPixelRight) return;

        int xPixelLeftInt = (int) xPixelLeft;
        int xPixelRightInt = (int) xPixelRight;

        if (xPixelLeftInt < 0) xPixelLeftInt = 0;
        if (xPixelRightInt > width) xPixelRightInt = width;

        double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
        double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
        double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
        double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

        double tex1 = 1 / rotLeftSideZ;
        double tex2 = 1 / rotRightSideZ;
        double tex3 = 0 / rotLeftSideZ;
        double tex4 = 8 / rotRightSideZ - tex3;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

            int xTexture = (int) ((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            int yPixelTopInt = (int) yPixelTop;
            int yPixelBottomInt = (int) yPixelBottom;

            if (yPixelTopInt < 0) yPixelTopInt = 0;
            if (yPixelBottomInt > height) yPixelBottomInt = height;

            for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
                double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                int yTexture = (int) (8 * pixelRotationY);
                pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256;
                zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
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
