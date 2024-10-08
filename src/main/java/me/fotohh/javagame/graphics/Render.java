package me.fotohh.javagame.graphics;

public class Render {

    public final int width;
    public final int height;
    public final int[] pixels;

    public Render(int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = new int[width*height];
    }

    public void draw(Render render, int xOffs, int yOffs){
        for(int y = 0; y < render.height; y++){
            int yPixel = y+yOffs;
            if(yPixel < 0 || yPixel >= height) continue;
            for(int x = 0; x < render.width; x++){
                int xPixel = x+xOffs;
                if(xPixel < 0 || xPixel >= width) continue;
                int renpix = render.pixels[x + y * render.width];
                if(renpix < 0) continue;
                pixels[xPixel + yPixel * width] = renpix;
            }
        }
    }

}
