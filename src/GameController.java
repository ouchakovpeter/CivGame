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

        // Mouse drag to move camera
        if (input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            float dx = (float) input.getDeltaX();
            float dy = (float) input.getDeltaY();
            camera.getPosition().sub(-dx, dy, 0);
        }

        // WASD or arrow keys can go here too
        //if (input.isKeyDown(GLFW_KEY_W)) {
        //    camera.getPosition().mul(0, 5, 0);
        //}
    }
}
