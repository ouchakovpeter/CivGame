package io;

import render.Camera;

import static org.lwjgl.glfw.GLFW.*;

public class GameController {
    private Camera camera;
    private Window window;

    private int movementSpeed = 5;

    private int zoom = 1;

    public GameController(Window window, Camera camera){
        this.window = window;
        this.camera = camera;
    }

    public void update() {
        Input input = window.getInput();

        // Close window
        if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }

        boolean moving = false;
        // Mouse drag to move camera
        if (input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            float dx = (float) input.getDeltaX();
            float dy = (float) input.getDeltaY();
            camera.getPosition().sub(-dx, dy, 0);
        }

        // WASD or arrow keys can go here too // turn this into a switch
        if (input.isKeyDown(GLFW_KEY_W)) {
            camera.getPosition().add(0, movementSpeed, 0);
        }
        if (input.isKeyDown(GLFW_KEY_S)) {
            camera.getPosition().add(0, -movementSpeed, 0);
        }
        if (input.isKeyDown(GLFW_KEY_D)) {
            camera.getPosition().add(movementSpeed, 0, 0);
        }
        if (input.isKeyDown(GLFW_KEY_A)) {
            camera.getPosition().add(-movementSpeed, 0, 0);
        }

        if (input.isKeyDown(GLFW_KEY_1)) {
            camera.getPosition().mul(0.5f);
        }

        if (input.isKeyDown(GLFW_KEY_2)) {
            camera.getPosition().mul(5);
        }

        if (input.getScrollY() != 0) {
            zoom += (float)input.getScrollY();
            if(zoom < 1){
                zoom = 1;
            }
            camera.setZoom(zoom);
        }
    }
}
