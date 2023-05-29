package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class ImportedObject extends Object{

    public ImportedObject(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, List<Vector3f> normal, Vector4f color) {
        super(shaderModuleDataList, vertices, normal, color);
        setupVAOVBO();

    }

    public void drawSetup(Camera camera, Projection projection){
        super.drawSetup(camera,projection);

        // Bind VBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);

        uniformsMap.setUniform("lightColor",new Vector3f(1.0f,1.0f,1.0f));
        uniformsMap.setUniform("lightPos",new Vector3f(1.0f,1.0f,1.0f));
    }


}
