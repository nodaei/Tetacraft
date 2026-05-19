package net.nodaei.tetacraft;

import net.nodaei.tetacraft.Render.Shader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String readFile(String path) {
        try (InputStream stream = Shader.class.getResourceAsStream(path)) {
            if (stream == null) throw new RuntimeException("Shader not found:" + path);

            return new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception exception) {
            throw new RuntimeException("Failed to load shader:" + path, exception);
        }
    }
}
