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
    private final float pitchSpeed = 0.1f;

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
//        if (input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
//            float dx = (float) input.getDeltaX();
//            float dy = (float) input.getDeltaY();
//            camera.getPosition().sub(dx*movementSpeed, -dy*movementSpeed, 0);
//        }

        // WASD or arrow keys can go here too // turn this into a switch
        float inputX = 0;
        float inputY = 0;

        if (input.isKeyDown(GLFW_KEY_W)) inputY -= 1;
        if (input.isKeyDown(GLFW_KEY_S)) inputY += 1;
        if (input.isKeyDown(GLFW_KEY_D)) inputX += 1;
        if (input.isKeyDown(GLFW_KEY_A)) inputX -= 1;

        // Normalize input vector
        float length = (float) Math.sqrt(inputX * inputX + inputY * inputY);
        if (length > 0) {
            inputX /= length;
            inputY /= length;
        }

        Vector3f right = new Vector3f(camera.getForward().y, -camera.getForward().x, 0); // perpendicular right vector

        Vector3f movement = new Vector3f();

        movement.fma(inputX * movementSpeed, camera.getForward());  // forward/backward
        movement.fma(inputY * movementSpeed, right);    // left/right

        camera.getPosition().add(movement);

        //z roll input
        if (input.isKeyDown(GLFW_KEY_Q)) {
            camera.addRoll(-rotationSpeed);
        }
        if (input.isKeyDown(GLFW_KEY_E)) {
            camera.addRoll(rotationSpeed);
        }


        if (input.getScrollY() != 0) {
            camera.addPitch(((float)input.getScrollY())*pitchSpeed);
        }
    }
}
