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

    private void resetMouseLocation(){
        if(hasFocus) {
            int middleX = display.getLocationOnScreen().x + Display.WIDTH / 2;
            int middleY = display.getLocationOnScreen().y + Display.HEIGHT / 2;
            try {
                Robot robot = new Robot();
                robot.mouseMove(middleX, middleY);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        int screenX = display.getLocationOnScreen().x;
        int screenY = display.getLocationOnScreen().y;

        int mouseX = e.getXOnScreen();
        int mouseY = e.getYOnScreen();

        if (mouseX > screenX && mouseX < screenX + Display.WIDTH) {
            MouseX = mouseX - screenX;
        } else {
            resetMouseLocation();
            return;
        }

        if (mouseY > screenY && mouseY < screenY + Display.HEIGHT) {
            MouseY = mouseY - screenY;
        } else {
            resetMouseLocation();
        }
    }
}
