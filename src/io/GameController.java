package io;

import render.*;
import world.*;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameController {
    private final Camera camera;
    private final Window window;
    private final World world;
    private float movementSpeed = 0.00001f;
    private final float rotationSpeed = 1.5f;
    private final float pitchSpeed = 1.0f;

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

        // Handle movement with rotation
        Vector3f movement = new Vector3f();

        // Forward/Backward movement (W/S)
        if (input.isKeyDown(GLFW_KEY_W)) movement.y = movementSpeed;
        if (input.isKeyDown(GLFW_KEY_S)) movement.y = -movementSpeed;

        // Strafe Left/Right (A/D)
        if (input.isKeyDown(GLFW_KEY_A)) movement.x = -movementSpeed;
        if (input.isKeyDown(GLFW_KEY_D)) movement.x = movementSpeed;

        // Normalize diagonal movement
        if (movement.lengthSquared() > 0) {
            movement.normalize().mul(movementSpeed);
            camera.addPosition(movement);
        }

        // Handle rotation with Q/E (yaw) and R/F (pitch)
        if (input.isKeyDown(GLFW_KEY_Q)) {
            camera.addYaw(-rotationSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_E)) {
            camera.addYaw(rotationSpeed);
        }

        // Handle pitch with R/F
        if (input.isKeyDown(GLFW_KEY_R)) {
            camera.addPitch(-pitchSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_F)) {
            camera.addPitch(pitchSpeed);
        }

        // Handle mouse dragging for camera movement
        if (input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            float dx = (float) input.getDeltaX();
            float dy = (float) input.getDeltaY();
            camera.moveBy(dx, dy);
        }

        // Speed boost with Shift
        movementSpeed = input.isKeyDown(GLFW_KEY_LEFT_SHIFT) ? 10.0f : 5.0f;
    }
}
