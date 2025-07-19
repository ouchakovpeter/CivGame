

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
public class Main {
    public static void main(String[] args) {

        //initializing glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW"); //error exception if GLFW doesn't load
        }

        long window = glfwCreateWindow(640, 480, "Window",0, 0);
        if  (window == 0) {
            throw new IllegalStateException("Failed to Create Window"); //error exception if window doesn't get created
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //finds the monitors size
        glfwSetWindowPos(window, (videoMode.width() - 640) / 2, (videoMode.height() - 480)/2); //puts the window in the center of the screen

        glfwMakeContextCurrent(window);//tells openGL to draw in this specific window

        //try to understand more
        GL.createCapabilities();//initializes LWJGL's OpenGL bindings for the current context / allows for communication to GPU and for java to use OpenGL

        while (!glfwWindowShouldClose(window)) { //keeps the window open

            glfwPollEvents(); //checks for events for player input for example

            if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
                glfwDestroyWindow(window);
            };

            glClear(GL_COLOR_BUFFER_BIT);//Clears the screen (erases whatever you drew last frame)

            glBegin(GL_QUADS);
                glColor4f(1,0,0,0);
                glVertex2f(-0.5f,0.5f);

                glColor4f(0,1,0,0);
                glVertex2f(0.5f,0.5f);

                glColor4f(0,0,1,0);
                glVertex2f(0.5f,-0.5f);

                glColor4f(1,1,1,0);
                glVertex2f(-0.5f,-0.5f);

            glEnd();

            glfwSwapBuffers(window);//allows the drawings to be seen on screen. ???
        }



        glfwTerminate();
    }
}
