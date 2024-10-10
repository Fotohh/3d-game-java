package me.fotohh.javagame.input;

import me.fotohh.javagame.display.Display;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {

    private final Display display;
    private boolean hasFocus = false;

    public InputHandler(Display display){
        this.display = display;
    }

    public boolean[] key = new boolean[68836];

    public static int MouseX = 0, MouseY = 0;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode > 0 && keyCode < key.length){
            key[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode > 0 && keyCode < key.length){
            key[keyCode] = false;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        hasFocus = true;
    }
    public void focusLost(FocusEvent e) {
        Arrays.fill(key, false);
        hasFocus = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        int middleX = Display.WIDTH / 2;
        int middleY = Display.HEIGHT / 2;

        //todo fix this code, it's not working properly

        if(e.getX() > 0 && e.getX() < Display.WIDTH){
            MouseX = e.getX();
        }else{
            if(hasFocus) {
                MouseX = middleX;
                try {
                    Robot robot = new Robot();
                    robot.mouseMove(display.getWindow().getLocationOnScreen().x + 50, display.getWindow().getLocationOnScreen().y + 50);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if(e.getY() >  0 && e.getY() < Display.HEIGHT) {
            MouseY = e.getY();
        }else{
            if(hasFocus) {
                MouseY = middleY;
                try {
                    Robot robot = new Robot();
                    robot.mouseMove(display.getWindow().getLocationOnScreen().x + 50, display.getWindow().getLocationOnScreen().y + 50);

                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
