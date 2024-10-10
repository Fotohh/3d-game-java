package me.fotohh.javagame.graphics;

import me.fotohh.javagame.log.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Texture {
    public static final Render floor = loadBitmap("/textures/tiles.png");
    private static HashMap<String, Render> textureCache = new HashMap<>();

    public static Render loadBitmap(String fileName) {
        if(textureCache == null) {
            textureCache = new HashMap<>();
        }
        if (textureCache.containsKey(fileName)) {
            return textureCache.get(fileName);
        }

        try {
            BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);
            textureCache.put(fileName, result);
            return result;
        } catch (Exception e) {
            Logger.lethal("Failed to load bitmap: " + fileName);
            throw new RuntimeException(e);
        }
    }
}
