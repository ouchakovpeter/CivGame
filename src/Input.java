import static org.lwjgl.glfw.GLFW.*;
public class Input {

    private long window;
    private boolean keys[];
    private boolean mouse[];
    private double mouseX, mouseY;
    private double lastMouseX, lastMouseY;
    private double deltaX, deltaY;

    public Input(long window){
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        for(int i = 0; i < GLFW_KEY_LAST; i++){
            keys[i] = false;
        }
        this.mouse = new boolean[GLFW_MOUSE_BUTTON_LAST];
        for(int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++){
            mouse[i] = false;
        }
    }

    public boolean isKeyDown(int key){
        return glfwGetKey(window, key) == 1;
    }
    public boolean isKeyPressed(int key){
        return(isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key){
        return(!isKeyDown(key) && keys[key]);
    }

    //glfwGetCursorPos()

    public boolean isMouseButtonDown(int button){
        return glfwGetMouseButton(window, button) == 1;
    }

    public boolean isMousePressed(int button) {
        return(isMouseButtonDown(button) && !mouse[button]);
    }

    public boolean isMouseReleased(int button) {
        return(!isMouseButtonDown(button) && mouse[button]);
    }

    public void update (){
        for(int i = 0; i < GLFW_KEY_LAST; i++){
            keys[i] = isKeyDown(i);
        }
        for(int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++){
            mouse[i] = isMouseButtonDown(i);
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;

        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window, x, y);
        mouseX = x[0];
        mouseY = y[0];

        deltaX = mouseX - lastMouseX;
        deltaY = mouseY - lastMouseY;
    }
    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
    public double getDeltaX() { return deltaX; }
    public double getDeltaY() { return deltaY; }

}
