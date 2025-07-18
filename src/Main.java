
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
public class Main {
    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW"); //error exception if GLFW doesn't load
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long window = glfwCreateWindow(640, 480, "My LWJGL Program",0, 0);
        if  (window == 0) {
            throw new IllegalStateException("Failed to Create Window"); //error exception if window doesn't get created
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //finds the monitors size
        glfwSetWindowPos(window, (videoMode.width() - 640) / 2, (videoMode.height() - 480)/2);

        glfwShowWindow(window);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }

        glfwTerminate();
    }
}
