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
        boolean turnRight = key[KeyEvent.VK_RIGHT];
        boolean turnLeft = key[KeyEvent.VK_LEFT];
        controls.tick(forward, backward, left, right, turnLeft, turnRight);
    }

}
