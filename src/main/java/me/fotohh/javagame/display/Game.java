package me.fotohh.javagame.display;

import me.fotohh.javagame.input.Controller;

import java.awt.event.KeyEvent;

public class Game {

    public int time;

    public Controller controls;

    public Game(){
        controls = new Controller();
    }

    public void tick(boolean[] key){
        time++;
        boolean forward = key[KeyEvent.VK_W];
        boolean backward = key[KeyEvent.VK_S];
        boolean left = key[KeyEvent.VK_A];
        boolean right = key[KeyEvent.VK_D];
        boolean jump = key[KeyEvent.VK_SPACE];
        controls.tick(forward, backward, left, right, jump);
    }

}
