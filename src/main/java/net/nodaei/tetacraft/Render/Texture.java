package net.nodaei.tetacraft.Render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int textureId;

    public Texture(String[] path) {
        textureId = load(path);
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);
    }

    public void delete() {
        glDeleteTextures(textureId);
    }

    private int load(String[] path) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, texture);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer ch = BufferUtils.createIntBuffer(1);

        int width = 0, height = 0;

        for (int i = 0; i < path.length; i++) {
            ByteBuffer image = stbi_load(path[i], w, h, ch, 4);
            if (image == null)
                throw new RuntimeException("Failed to load texture: " + path[i] + " | " + stbi_failure_reason());

            if (i == 0) {
                width = w.get(0); height = h.get(0);

                glTexImage3D(
                        GL_TEXTURE_2D_ARRAY,
                        0,
                        GL_RGBA8,
                        width,
                        height,
                        path.length,
                        0,
                        GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        (ByteBuffer) null
                );

            } else if (w.get(0) != width || h.get(0) != height) {
                stbi_image_free(image);
                throw new RuntimeException("All texture array images must have the same size.");
            }

            glTexSubImage3D(
                    GL_TEXTURE_2D_ARRAY,
                    0,
                    0,
                    0,
                    i,
                    width,
                    height,
                    1,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    image
            );

            stbi_image_free(image);
        }

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);

        return texture;
    }
}