package me.fotohh.javagame.graphics;

import me.fotohh.javagame.log.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Texture {
    public static Render floor = loadBitmap("/textures/tiles.png");

    public static Render loadBitmap(String fileName) {
        try {
            BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);
            return result;
        } catch (Exception e) {
            Logger.lethal("Failed to load bitmap: " + fileName);
            throw new RuntimeException(e);
        }
    }
}
