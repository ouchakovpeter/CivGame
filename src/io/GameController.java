package io;

import render.*;
import world.*;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameController {
    private final Camera camera;
    private final Window window;
    private final World world;
    private float movementSpeed = 0.1f;
    private final float rotationSpeed = 0.01f;
    private final float pitchSpeed = 0.01f;

    public GameController(Window window, Camera camera, World world) {
        this.window = window;
        this.camera = camera;
        this.world = world;
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
            camera.getPosition().sub(dx*movementSpeed, -dy*movementSpeed, 0);
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

        if (input.isKeyDown(GLFW_KEY_Q)) {
            camera.addRoll(-rotationSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_E)) {
            camera.addRoll(rotationSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_R)) {
            camera.addPitch(-pitchSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_F)) {
            camera.addPitch(+pitchSpeed);
        }

        if (input.getScrollY() != 0) {
            
        }
    }
}
