package me.fotohh.javagame.graphics;

import me.fotohh.javagame.display.Game;

public class Screen extends Render{

    private final Render3D render;

    public Screen(int width, int height, Game game) {
        super(width, height);
        render = new Render3D(width, height, game);
    }

    public void render(){
        for(int i = 0; i < width * height; i++){
            pixels[i] = 0;
        }
        render.floor();
        render.renderWall(0, 0.5, 1.5, 1.5, 0);
        render.renderDistanceLimiter();

        draw(render, 0, 0);
    }
}
