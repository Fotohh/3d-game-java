package me.fotohh.javagame.input;

public class Controller {

    public double x, y, z, rotation, xa, za, rotationa;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean sprinting = false;
    public static boolean moving = false;
    public static boolean crouching = false;

    public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
        double rotationSpeed = 0.025;
        double walkSpeed = 0.5;
        double sprintSpeed = 0.5;
        double xMove = 0;
        double zMove = 0;
        double jumpHeight = 0.7;
        double crouchHeight = 0.3;

        if (forward) {
            zMove++;
        }
        if (back) {
            zMove--;
        }
        if (left) {
            xMove--;
        }
        if (right) {
            xMove++;
        }
        if (turnLeft) {
            rotationa -= rotationSpeed;
        }
        if (turnRight) {
            rotationa += rotationSpeed;
        }
        if (jump) {
            y += jumpHeight;
        }
        if (crouch) {
            y -= crouchHeight;
            walkSpeed -= 0.2;
            crouching = true;
        }else{
            crouching = false;
        }
        if (sprint && (!crouch && !jump)) {
            sprinting = true;
            walkSpeed += sprintSpeed;
        }else{
            sprinting = false;
        }
        moving = xMove != 0 || zMove != 0;

        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);

        xa += (xMove * cos + zMove * sin) * walkSpeed;
        za += (zMove * cos - xMove * sin) * walkSpeed;

        x += xa;
        y *= 0.9;
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotation *= 0.5;
    }
}
