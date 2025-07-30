package render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static  org.lwjgl.opengl.GL20.*;

//this class is important as it allows the graphics to be computed by the gpu. it tells the graphics card where to place vertices (vertex shader), how to color pixels (fragment shader)
public class Shader {
    private int program; //vertex + fragment shader
    private int vs; //vertex shader
    private int fs; //fragment shader

    public Shader (String filename) {
        program = glCreateProgram(); //Creates an empty "container" (program) to hold shaders

        //Load & Compile Vertex render.Shader
        vs = glCreateShader(GL_VERTEX_SHADER); // Creates a vertex shader slot
        //the GPU allocates a small space in its memory specifically for vertex shader instructions
        glShaderSource(vs, readFile(filename + ".vs"));  //loads the shader source code from shader.vs
        glCompileShader(vs); //compiles the code into GPU instructions
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) { //If compilation fails, prints error
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        //same as the vertex shader but for colors instead of vertices
        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        //combines both shaders into the program, now the gpu knows where to put vertices, and how to color them.
        glAttachShader(program, vs);
        glAttachShader(program, fs);

        //Link Attributes (Vertex Data)
        //this tells openGL attribute 0 is vertex position, and attribute 1 is textures.
        //Matches the render.Model class's vertex data
        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program); //combines shaders into a final GPU-ready program
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program); //checks for compatibility errors,
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1){
            glUniform1i(location, value);
        }
    }
    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1){
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    //Tells OpenGL: "Use this shader for all upcoming drawings."
    public void bind(){
        glUseProgram(program);
    }

    //Reads shader.vs or shader.fs from the shaders folder. we use this code above.
    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br;

        try{
            br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
            String line;
            while((line = br.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            br.close();
        }
        catch(IOException e) { //If the file is missing, print an error
            e.printStackTrace();
        }
        return string.toString();
    }
}
