import static org.lwjgl.glfw.GLFW.*;

public class GameController {
    private Camera camera;
    private Window window;

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

        // WASD or arrow keys can go here too
        if (input.isKeyDown(GLFW_KEY_W)) {
            camera.getPosition().add(0, 1, 0);
        }
        if (input.isKeyDown(GLFW_KEY_S)) {
            camera.getPosition().add(0, -1, 0);
        }
        if (input.isKeyDown(GLFW_KEY_D)) {
            camera.getPosition().add(1, 0, 0);
        }
        if (input.isKeyDown(GLFW_KEY_A)) {
            camera.getPosition().add(-1, 1, 0);
        }
//        if (input.isKeyPressed(GLFW_KEY_E)) {
//            camera.getPosition().mul((float)Math.toRadians(0), (float)Math.toRadians(90), (float)Math.toRadians(0));
//        }
//        if (input.isKeyDown(GLFW_KEY_Q)) {
//            camera.getPosition().add(-1, 1, 0);
//        }
    }
}
