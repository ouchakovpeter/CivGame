package io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;

    private int width, height;
    private boolean fullscreen;
    private Input input;

    public static void setCallbacks(){
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }
    public Window(){
        setSize(640,480);
        setFullscreen(false);
    }

    public void createWindow(String title){
        window = glfwCreateWindow(
                width,
                height,
                title,
                fullscreen ? glfwGetPrimaryMonitor() : 0,
                0);

        if  (window == 0) {
            throw new IllegalStateException("Failed to Create io.Window"); //error exception if window doesn't get created
        }

        if(!fullscreen) {
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //finds the monitors size
            glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2); //puts the window in the center of the screen
            glfwShowWindow(window);
        }

        glfwMakeContextCurrent(window);//tells openGL to draw in this specific window

        input = new Input(window);
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(window) != false;
    }

    public void swapBuffers(){
        glfwSwapBuffers(window);//allows the drawings to be seen on screen. ???
        //swaps the front/back buffers, The new frame becomes visible, The old frame becomes the new back buffer.
        //a buffer is just a temporary storage area
        //OpenGL uses two buffers, this way opengl can draw the entire frame in secret then instantly swap it to the front when done
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setFullscreen(boolean fullscreen){
        this.fullscreen = fullscreen;
    }

    public void update(){
        input.update();
        glfwPollEvents();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean isFullscreen(){
        return fullscreen;
    }

    public long getWindow(){
        return window;
    }

    public Input getInput(){
        return input;
    }

}
