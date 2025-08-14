package render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31C.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class Model {
    private int draw_count;
    private int v_id; //vertex id
    private int t_id; //texture id

    private int i_id; //indices id
    private int instance_id; // VBO for per-instance data

    public Model(float[] vertices, float[] tex_coords, int[] indices){
        draw_count = indices.length;

        // Initialize instance buffer
        instance_id = glGenBuffers();
        
        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render(){
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0,0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public void renderInstanced(float[] instanceData, int instanceCount, Camera cam, Shader shader) {
        if (instanceCount <= 0 || instanceData == null || instanceData.length == 0) return;

        // Set up shader uniforms
        shader.setUniform("projection", cam.getProjection());
        shader.setUniform("view", cam.getViewMatrix());

        // Enable per-vertex attributes
        glEnableVertexAttribArray(0); // position
        glEnableVertexAttribArray(1); // texcoord

        // Bind and set per-vertex attributes
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        // Upload per-instance data (x, y, z, brightness)
        glBindBuffer(GL_ARRAY_BUFFER, instance_id);
        FloatBuffer instBuf = createBuffer(instanceData);
        glBufferData(GL_ARRAY_BUFFER, instBuf, GL_DYNAMIC_DRAW);

        // Attribute location 2 = vec4(x, y, z, brightness), advances per instance
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribDivisor(2, 1);

        // Bind indices and draw instances
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElementsInstanced(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0, instanceCount);

        // Cleanup
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        
        // Reset divisor
        glVertexAttribDivisor(2, 0);
    }

    private FloatBuffer createBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int getInstance_id() {
        return instance_id;
    }
}
