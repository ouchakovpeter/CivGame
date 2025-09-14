package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import entities.MobManager;
import entities.flat.base.FlatRenderer;
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

        Window win = new Window();//Creates a new instance of a Window object
        win.setSize(1280, 720);//sets its size
        win.setFullscreen(false);//Not Fullscreen
        win.createWindow("CivGame");//creates and displays the window with the title

        glfwSwapInterval(0); //disable vsync, sets the minimum number of screen refreshes between buffer swaps, can cause screen tearing

        GL.createCapabilities();//initializes LWJGL's OpenGL bindings for the current context / allows for communication to GPU and for java to use OpenGL /
        // Loads OpenGL functions for Java to use.

        NoiseGenerator generation = new NoiseGenerator(40,40, 20);//init noise with set settings and a set world size.
        World world = new World(generation); //set world size, generate noise, assign depth and tile texture.
        Camera camera = new Camera(win.getWidth(), win.getHeight(), world);
        MobManager mobManager = new MobManager();

        GameController controller = new GameController(win, camera, world, mobManager); //takes in the camera, window and world to control it.
        Shader shader = new Shader("shader"); // loads and compiles shader files (shader.vs and shader.fs).
        Shader flatShader = new Shader("flatshader");

        FlatRenderer flats = new FlatRenderer(camera);
        TileRenderer tiles = new TileRenderer();

        mobManager.spawnHuman(world);

        glfwSetFramebufferSizeCallback(win.getWindow(), (window, width, height) -> {
            glViewport(0, 0, width, height);
            camera.updateProjection(width, height);
        });

        glEnable(GL_DEPTH_TEST); //fragments (pixels) closer to the camera hide those behind them
        glEnable(GL_BLEND);//allows transparent objects (or semi-transparent textures) to render properly
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //Defines how blending works, (source color × alpha) + (destination color × (1 - alpha))

        double frame_cap = 1.0/120.0;

        double frame_time = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!win.shouldClose()) { //keeps the window open / rendering loop

            //System.out.println(camera.getRoll());

            boolean can_render = false;

            double time_2 = Timer.getTime();
            double deltaTime = time_2 - time; //delta time
            unprocessed += deltaTime; //fixes unprocessed time;

            frame_time += deltaTime;

            time = time_2;

                while (unprocessed >= frame_cap){
                    unprocessed -= frame_cap;
                    can_render = true;

                    controller.update(deltaTime);

                    // Advance mob simulation on the fixed timestep so timers accumulate correctly
                    mobManager.update(frame_cap, world);

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

                    camera.wrap(world);

                    world.render(tiles, flats, shader, flatShader,camera, win);
                    mobManager.render(flats, flatShader, camera);

                    win.swapBuffers();
                    frames++;
                }
        }

        glfwTerminate();
    }
}
