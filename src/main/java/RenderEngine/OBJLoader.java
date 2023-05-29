package RenderEngine;

import Engine.ImportedObject;
import Engine.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Engine.Object;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;


public class OBJLoader {
    public static ImportedObject loadObjModel(String fileName) throws IOException {
        FileReader file = null;
        try {
            file = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("file tidak ditemukan");
            String currentPath = new java.io.File(".").getCanonicalPath();
            System.out.println(currentPath);
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(file);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        List<Vector3f> verticesArray = null;
        List<Vector3f> normalsArray = null;
//        float[] normalsArray = null;
        float[] texturesArray = null;
        int[] indicesArray = null;
        int indicesCount = 0;

        try {
            while (true) {
                line =  reader.readLine();
//                System.out.println("line: " + line);
                if (line != null) {
                    String[] currentLine = line.split(" ");

                    if (line.startsWith("v ")) {
//                        System.out.println("V");
                        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        vertices.add(vertex);
                    } else if (line.startsWith("vt ")) {
//                        System.out.println("VT");
                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                        textures.add(texture);
                    } else if (line.startsWith("vn ")) {
//                        System.out.println("VN");
                        System.out.println("entry normal: " + line);
                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(normal);
//                        System.out.println("normal array: " + normals);
                    } else if (line.startsWith("f ")) {
                        texturesArray = new float[vertices.size() * 2];
//                    normalsArray = new float[vertices.size() * 3];
                        normalsArray = new ArrayList<>();
//                        for (int i = 0; i < vertices.size(); i++) {
//                            normalsArray.add(new Vector3f());
//                        }
//                        System.out.println(normalsArray);
//                    break;

                        while (line != null) {
//                        System.out.println("loop f");
//                            if (!line.startsWith("f ")) {
//                                line = reader.readLine();
//                                continue;
//                            } else if (line.startsWith("g ")) {
//                                break;
//                            }

                            if (line.startsWith("o ")) {
                                System.err.println("break");
                                break;
                            } else if (line.startsWith("g ")) {
                                break;
                            }

//                            System.out.println("F");
//                            System.out.println("vertices: " + vertices.size());
//                            System.out.println("Textures: " + textures.size());
//                            System.out.println("normals: " + normals.size());

                            String[] currentLineF = line.split(" ");
//                            System.out.println("vertex data: " + Arrays.toString(currentLineF));
                            if  (line.startsWith("f ")) {
                                String[] vertex1 = currentLineF[1].split("/");
                                String[] vertex2 = currentLineF[2].split("/");
                                String[] vertex3 = currentLineF[3].split("/");


                                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
                                indicesCount++;
                            }
                            line = reader.readLine();
                        }
                    }
                } else {
                    break;
                }
                if (line == null) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("indices count: " + indices.size());
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        verticesArray = new ArrayList<>();
        for(int i:indices) {
            verticesArray.add(vertices.get(i));
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

//        System.out.println("indices: " + indices);
//        System.out.println("vertices Array: " + verticesArray + "size: " + verticesArray.size());
        ImportedObject object = new ImportedObject(Arrays.asList(
                //shaderFile lokasi menyesuaikan objectnya
                new ShaderProgram.ShaderModuleData
                        ("resources/shaders/scene.vert"
                                , GL_VERTEX_SHADER),
                new ShaderProgram.ShaderModuleData
                        ("resources/shaders/scene.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                verticesArray,
                normalsArray,
                new Vector4f(0.0f,1.0f,1.0f,1.0f));

        System.out.println("normal array: " + normalsArray);
        return object;
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, List<Vector3f> normalArray) {
//        System.out.println("vertex data: " + Arrays.toString(vertexData));
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
//        System.out.println(currentVertexPointer);
        indices.add(currentVertexPointer);

//        int indexTexture = Integer.parseInt(vertexData[1]) - 1;
//        Vector2f currentTex = textures.get(indexTexture);
//        System.out.println("current tex: " + indexTexture);
//        textureArray[currentVertexPointer * 2] = currentTex.x;
//        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

        int indexNormal = Integer.parseInt(vertexData[2]) - 1;
        System.out.println("index normal: " + indexNormal);
        Vector3f currentNorm = new Vector3f();
        currentNorm.x = normals.get(indexNormal).x;
        currentNorm.y = normals.get(indexNormal).y;
        currentNorm.z = normals.get(indexNormal).z;
        System.out.println("current normal: " + currentNorm);
        normalArray.add(currentNorm);
    }
}
