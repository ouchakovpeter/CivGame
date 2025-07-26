

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
public class Main {
    public static void main(String[] args) {

        //initializing glfw / Tries to start GLFW (the library that handles windows).
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
        GL.createCapabilities();//initializes LWJGL's OpenGL bindings for the current context / allows for communication to GPU and for java to use OpenGL /
        // Loads OpenGL functions for Java to use.

        Camera camera = new Camera(640, 480);
        glEnable(GL_TEXTURE_2D);
        //Enables 2D textures

        //defining a quad / square by defining the 4 corners in 3d space. / -0.5 means the square is 1 unit tall in width and height centered at 0,0
        float[] vertices = new float[] {
                -0.5f, 0.5f, 0,  // top left      0
                0.5f, 0.5f, 0,   // top right     1
                0.5f, -0.5f, 0,  // bottom right  2
                -0.5f, -0.5f, 0, // top left      3
        };

        //uv texture coordinates
        float[] texture = new float[]{
                1,1, // Top-left corner
                0,1, // Top-right corner
                0,0, // Bottom-right corner
                1,0, // Bottom-left corner
        };

        //draws the triangles / splits the square into two triangles // these indicies refer to the order fo the vertices // indices save memory by reusing vertices
        int [] indices = new int[]{
                0,1,2, // First triangle (top-left, top-right, bottom-right)
                2,3,0  // Second triangle (bottom-right, bottom-left, top-left)
        };

        //shaders are important to calculate where vertices are on screen/ vertex shader puts where vertices appear on the screen/ fragment shader colors each pixel
        Model model = new Model(vertices, texture, indices); //stores the shape (vertices, texture coords, indices).
        Shader shader = new Shader("shader"); // loads and compiles shader files (shader.vs and shader.fs).
        Texture tex = new Texture("res/smile.png"); // loads an image (smile.png) from the res folder.

        Matrix4f projection = new Matrix4f().ortho2D(-640/2, 640/2, -480/2, 480/2);
        Matrix4f scale = new Matrix4f().scale(64);
        Matrix4f target = new Matrix4f();

        while (!glfwWindowShouldClose(window)) { //keeps the window open / rendering loop

            target = scale;

            glfwPollEvents(); //checks for events for player input for example

            if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) { //checks for player input.
                glfwDestroyWindow(window);
                break;
            };

            glClear(GL_COLOR_BUFFER_BIT);//Clears the screen (erases whatever you drew last frame)

            //binding = Selecting a texture for OpenGL to use, so we must bind so that
            //already compiled shaders.
            shader.bind(); // activates the shader // runs a calculation by the gpu
            shader.setUniform("sampler", 0); // Look in slot 0 for the texture
            shader.setUniform("projection", camera.getProjection().mul(target));
            tex.bind(0);// Put the texture in slot 0
            model.render(); //draws the square

            glfwSwapBuffers(window);//allows the drawings to be seen on screen. ???
            //swaps the front/back buffers, The new frame becomes visible, The old frame becomes the new back buffer.
            //a buffer is just a temporary storage area
            //OpenGL uses two buffers, this way opengl can draw the entire frame in secret then instantly swap it to the front when done
        }

        glfwTerminate();
    }
}
