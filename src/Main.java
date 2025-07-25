

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
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

        glEnable(GL_TEXTURE_2D);

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0, //top left      0
                0.5f, 0.5f, 0, //top right      1
                0.5f, -0.5f, 0, //bottom right  2
                -0.5f, -0.5f, 0, //top left      3
        };

        float[] texture = new float[]{
                1,1,
                0,1,
                0,0,
                1,0,
        };

        int [] indices = new int[]{
                0,1,2,
                2,3,0
        };

        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");


        Texture tex = new Texture("res/smile.png");

        Matrix4f projection = new Matrix4f().ortho2D(-640/2, 640/2, -480/2, 480/2);
        Matrix4f scale = new Matrix4f().scale(64);
        Matrix4f target = new Matrix4f();

        projection.mul(scale, target);

        while (!glfwWindowShouldClose(window)) { //keeps the window open / rendering loop

            glfwPollEvents(); //checks for events for player input for example

            if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
                glfwDestroyWindow(window);
                break;
            };

            glClear(GL_COLOR_BUFFER_BIT);//Clears the screen (erases whatever you drew last frame)


            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", target);
            tex.bind(0);
            model.render();

//            glBegin(GL_QUADS);
//                glTexCoord2f(1,1);
//                glVertex2f(-0.5f,0.5f);
//
//                glTexCoord2f(0,1);
//                glVertex2f(0.5f,0.5f);
//
//                glTexCoord2f(0,0);
//                glVertex2f(0.5f,-0.5f);
//
//                glTexCoord2f(1,0);
//                glVertex2f(-0.5f,-0.5f);

            glfwSwapBuffers(window);//allows the drawings to be seen on screen. ???
        }



        glfwTerminate();
    }
}
