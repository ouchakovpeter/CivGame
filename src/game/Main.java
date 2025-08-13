package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import io.*;
import render.*;
import world.*;

import org.lwjgl.opengl.GL;


public class Main {
    public static void main(String[] args) {

        //initializing glfw / Tries to start GLFW (the library that handles windows).
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW"); //error exception if GLFW doesn't load
        }

        Window win = new Window();
        win.setSize(1280, 720);
        win.setFullscreen(false);
        win.createWindow("CivGame");
        WorldGenerator generation = new WorldGenerator(1000,1000, 10);
        World world = new World(generation);
        Camera camera = new Camera(win.getWidth(), win.getHeight(), world);

        glfwSetFramebufferSizeCallback(win.getWindow(), (window, width, height) -> {
            glViewport(0, 0, width, height);
            camera.updateProjection(width, height);
        });

        //try to understand more
        GL.createCapabilities();//initializes LWJGL's OpenGL bindings for the current context / allows for communication to GPU and for java to use OpenGL /
        // Loads OpenGL functions for Java to use.

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        //should be part of a "world class"
        TileRenderer tiles = new TileRenderer();

        Shader shader = new Shader("shader"); // loads and compiles shader files (shader.vs and shader.fs).

        // After creating the camera and world

        GameController controller = new GameController(win, camera, world);

        double frame_cap = 1.0/120.0;

        double frame_time = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!win.shouldClose()) { //keeps the window open / rendering loop

            //System.out.println(camera.getRoll());

            boolean can_render = false;

            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed; //fixes unprocessed time;

            frame_time += passed;

            time = time_2;

                while (unprocessed >= frame_cap){
                    unprocessed -= frame_cap;
                    can_render = true;

                    controller.update();
                    //world.correctCamera(camera,win);

                    win.update(); //checks for events for player input for example

                    //frame rate counter
                    if(frame_time >= 1.0){
                        frame_time = 0;
                        System.out.println("FPS:" + frames);
                        frames = 0;
                    }
                }

                if(can_render){
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);//Clears the screen (erases whatever you drew last frame)

                    world.render(tiles, shader, camera, win);

                    win.swapBuffers();
                    frames++;
                }
        }

        glfwTerminate();
    }
}
