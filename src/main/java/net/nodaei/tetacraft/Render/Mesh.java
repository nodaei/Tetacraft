package net.nodaei.tetacraft.Render;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId;

    private final float[] vertices = {
            // front
            -0.5f,  0.5f, 0.5f, 0.0f, 1.0f, 0, // V0
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0, // V1
             0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0, // V2
             0.5f,  0.5f, 0.5f, 1.0f, 1.0f, 0, // V3
            // back
            -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 0, // V4
             0.5f,  0.5f, -0.5f, 0.0f, 1.0f, 0, // V5
            -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0, // V6
             0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0, // V7
            //x     y       z     u     v    layer
    };

    // the order of which the vertices are getting rendered.
    private final int[] indices = {
            0, 1, 3, 3, 1, 2, // front face
            4, 0, 3, 5, 4, 3, // top face
            3, 2, 7, 5, 3, 7, // right face
            6, 1, 0, 6, 0, 4, // left face
            2, 1, 6, 2, 6, 7, // bottom face
            7, 6, 4, 7, 4, 5, // back face
    };

    public void init() {
        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        eboId = glGenBuffers();

        glBindVertexArray(vaoId);

        // Upload Vertex data
        FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertices.length);
        vertexData.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        MemoryUtil.memFree(vertexData);

        // Upload Index data
        IntBuffer indexData = MemoryUtil.memAllocInt(indices.length);
        indexData.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        MemoryUtil.memFree(indexData);

        // 6 is the amount of bytes per vertex.
        // (x, y, z, u, v, layer)
        int stride = 6 * Float.BYTES;

        // Assigns data to the shader
        // x, y, z
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);
        // u, v
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);
        // layer
        glVertexAttribPointer(2, 1, GL_FLOAT, false, stride, 5L * Float.BYTES);
        glEnableVertexAttribArray(2);

        // cleaning
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
    }
}
