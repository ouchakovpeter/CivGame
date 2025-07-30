package render;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        // Load image
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            IntBuffer channels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            image = stbi_load("texture/" + filename, w, h, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture " + filename + ": " + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        // Generate texture
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Upload data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free image memory
        stbi_image_free(image);
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public int getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
