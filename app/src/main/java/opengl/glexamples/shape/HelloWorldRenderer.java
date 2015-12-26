package opengl.glexamples.shape;

/**
 * Created by deliaqi on 2015/12/26.
 */
import java.io.*;
import java.nio.*;

import javax.microedition.khronos.opengles.*;
import android.content.Context;
import android.graphics.*;
import javax.microedition.khronos.egl.EGLConfig;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.*;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

import opengl.glexamples.R;

import opengl.glexamples.glActivity.LightOpenglActivity_1;

public class HelloWorldRenderer implements Renderer {
    public HelloWorldRenderer(LightOpenglActivity_1 main) {
        this.context = main;
        createBuffers();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glClearDepthf(1f);

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CCW);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[] { 1.0f, 0.0f,
                0.0f, 1.0f }, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[] { 1.0f, 1.0f,
                1.0f, 1.0f }, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[] { 0.0f,
                0.0f, 1.0f, 1.0f }, 0);

        this.loadTexture(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 宽高比
        float aspect = (float) width / (float) (height == 0 ? 1 : height);

        // 设置视口
        gl.glViewport(0, 0, width, height);

        // 设置当前矩阵堆栈为投影矩阵，并将矩阵重置为单位矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        // 设置投影变换矩阵
        GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 200.0f);
        GLU.gluLookAt(gl, 30f, 30f, 30f, 0f, 0f, 0f, 0, 1, 0);
    }

    public void onDrawFrame(GL10 gl) {
        // 清除颜色缓冲
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // 反方向水平旋转下灯光位置
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(-angley, 0, 1, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[] { 50, 50,
                50, 0 }, 0);

        // 旋转模型
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(anglez, 0, 0, 1);
        gl.glRotatef(angley, 0, 1, 0);
        gl.glRotatef(anglex, 1, 0, 0);

        // 启用顶点数组、纹理坐标、法向量数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        // 设置顶点、纹理坐标、法向量数据
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tvertices);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normals);

        // 绑定纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);

        // 绘制
        gl.glDrawElements(GL10.GL_TRIANGLES, triangles.remaining(),
                GL10.GL_UNSIGNED_BYTE, triangles);

        // 禁用顶点、纹理坐标、法向量数组
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

        // 递增角度值以便每次以不同角度绘制
        anglex += 0.1;
        angley += 0.2;
        anglez += 0.3;
    }

    private void createBuffers() {
        // 创建顶点缓冲
        vertices = ByteBuffer.allocateDirect(data_vertices.length * 4);
        vertices.order(ByteOrder.nativeOrder());
        vertices.asFloatBuffer().put(data_vertices);
        vertices.position(0);

        // 创建索引缓冲
        triangles = ByteBuffer.allocateDirect(data_triangles.length * 2);
        triangles.put(data_triangles);
        triangles.position(0);

        // 创建纹理坐标缓冲
        tvertices = ByteBuffer.allocateDirect(data_tvertices.length * 4);
        tvertices.order(ByteOrder.nativeOrder());
        tvertices.asFloatBuffer().put(data_tvertices);
        tvertices.position(0);

        normals = ByteBuffer.allocateDirect(data_normals.length * 4);
        normals.order(ByteOrder.nativeOrder());
        normals.asFloatBuffer().put(data_normals);
        normals.position(0);
    }

    private void loadTexture(GL10 gl) {
        InputStream bitmapStream = null;
        Bitmap bitmap = null;
        try {
            // 打开图片资源流
            /*
            bitmapStream = context.getResources().openRawResource(
                    R.drawable.card_back);
            // 解码图片生成 Bitmap 实例
            bitmap = BitmapFactory.decodeStream(bitmapStream);
            */
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.card_front) ;

            // 生成一个纹理对象，并将其ID保存到成员变量 texture 中
            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            texture = textures[0];

            // 将生成的空纹理绑定到当前2D纹理通道
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);

            // 设置2D纹理通道当前绑定的纹理的属性
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_REPEAT);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_REPEAT);

            // 将bitmap应用到2D纹理通道当前绑定的纹理中
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        } finally {
            // 释放资源
            // BTW: 期待 android 早日支持 Java 新的 try-with-resource 语法

            if (bitmap != null)
                bitmap.recycle();

            if (bitmapStream != null) {
                try {
                    bitmapStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private Context context;

    private ByteBuffer vertices;
    private ByteBuffer triangles;
    private ByteBuffer tvertices;
    private ByteBuffer normals;

    private int texture;

    private float anglex = 0f;
    private float angley = 0f;
    private float anglez = 0f;

    private float[] data_vertices = { -5.0f, -5.0f, -5.0f, -5.0f, 5.0f, -5.0f,
            5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f,
            -5.0f, -5.0f, -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f,
            5.0f, 5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f,
            -5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f, 5.0f, -5.0f,
            -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f,
            -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
            5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f,
            -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f,
            -5.0f, };

    private float[] data_tvertices = { 1.0000f, 1.0000f, 1.0000f, 0.0000f,
            0.0000f, 0.0000f, 0.0000f, 0.0000f, 0.0000f, 1.0000f, 1.0000f,
            1.0000f, 0.0000f, 1.0000f, 1.0000f, 1.0000f, 1.0000f, 0.0000f,
            1.0000f, 0.0000f, 0.0000f, 0.0000f, 0.0000f, 1.0000f, 0.0000f,
            1.0000f, 1.0000f, 1.0000f, 1.0000f, 0.0000f, 1.0000f, 0.0000f,
            0.0000f, 0.0000f, 0.0000f, 1.0000f, 0.0000f, 1.0000f, 1.0000f,
            1.0000f, 1.0000f, 0.0000f, 1.0000f, 0.0000f, 0.0000f, 0.0000f,
            0.0000f, 1.0000f, 0.0000f, 1.0000f, 1.0000f, 1.0000f, 1.0000f,
            0.0000f, 1.0000f, 0.0000f, 0.0000f, 0.0000f, 0.0000f, 1.0000f,
            0.0000f, 1.0000f, 1.0000f, 1.0000f, 1.0000f, 0.0000f, 1.0000f,
            0.0000f, 0.0000f, 0.0000f, 0.0000f, 1.0000f, };
    private byte[] data_triangles = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 33, 34, 35, };

    private float[] data_normals = { -5.0f, -5.0f, -5.0f, -5.0f, 5.0f, -5.0f,
            5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f,
            -5.0f, -5.0f, -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f,
            5.0f, 5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f,
            -5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f, 5.0f, -5.0f,
            -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, -5.0f, 5.0f,
            -5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
            5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f,
            -5.0f, 5.0f, -5.0f, -5.0f, 5.0f, -5.0f, 5.0f, 5.0f, -5.0f, 5.0f,
            -5.0f, };
}



