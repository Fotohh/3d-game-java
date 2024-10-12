package me.fotohh.javagame.input;

import me.fotohh.javagame.display.Display;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {

    private final Robot robot;
    private final Display display;
    private boolean hasFocus = false;

    public InputHandler(Display display) {
        this.display = display;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean[] key = new boolean[68836];

    public static int MouseX = 0, MouseY = 0;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = false;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        hasFocus = true;
        centerMouse();
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
        updateMousePosition(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        updateMousePosition(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateMousePosition(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateMousePosition(e);
    }

    private void updateMousePosition(MouseEvent e) {
        int screenX = display.getToolkit().getScreenSize().width;
        int screenY = display.getToolkit().getScreenSize().height;
        int windowWidth = display.getWindow().getWidth();
        int windowHeight = display.getWindow().getHeight();

        int mouseX = e.getXOnScreen();
        int mouseY = e.getYOnScreen();

        if (mouseX < 0 || mouseX > screenX || mouseY < 0 || mouseY > screenY) {
            centerMouse();
            MouseX = windowWidth / 2;
            MouseY = windowHeight / 2;
        } else {
            MouseX = mouseX - screenX;
            MouseY = mouseY - screenY;
        }
    }

    private void centerMouse() {
        int screenX = display.getWindow().getX();
        int screenY = display.getWindow().getY();
        int windowWidth = display.getWindow().getWidth();
        int windowHeight = display.getWindow().getHeight();

        int centerX = screenX + windowWidth / 2;
        int centerY = screenY + windowHeight / 2;

        robot.mouseMove(centerX, centerY);
        MouseX = windowWidth / 2;
        MouseY = windowHeight / 2;
    }
}
