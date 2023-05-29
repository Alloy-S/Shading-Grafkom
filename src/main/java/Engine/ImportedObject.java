package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES20.GL_FLOAT;

public class ImportedObject extends Object{

    int ibo;
    List<Integer> index = new ArrayList<>();
    public ImportedObject(List<ShaderModuleData> shaderModuleDataList,List<Integer> index, List<Vector3f> vertices, List<Vector3f> normal, Vector4f color) {
        super(shaderModuleDataList, vertices, normal, color);
        this.index = index;

//        for (int i = 0; i< index.size();i++
//             ) {
//            this.index.set(i,i);
//        }
        System.out.println("index obj: " + this.index);
        System.out.println("vertice array: " + this.vertices);
        System.out.println("normal array: " + this.normal);
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
        uniformsMap.setUniform("lightPos",new Vector3f(0.0f,10.0f,1.0f));
    }

    public void setupVAOVBO()
    {
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);

        //set nbo
        nbo = glGenBuffers();
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,Utils.listoInt(index),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(normal), GL_STATIC_DRAW);

        uniformsMap.createUniform("lightColor");
        uniformsMap.createUniform("lightPos");
    }

//    public void draw(Camera camera, Projection projection) {
//        drawSetup(camera, projection);
//        // Draw the vertices
//        //optional
//        glLineWidth(10); //ketebalan garis
//        glPointSize(10); //besar kecil vertex
//        glDrawElements(GL_TRIANGLES, vertices.size(),GL_UNSIGNED_INT,0);
//        for (Object child : childObject) {
//            child.draw(camera, projection);
//        }
//    }



}
