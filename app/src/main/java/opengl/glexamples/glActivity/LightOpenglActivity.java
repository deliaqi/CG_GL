package opengl.glexamples.glActivity;
import opengl.glexamples.R;
import opengl.glexamples.shape.initBitmap;
import opengl.glexamples.shape.LightRender;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class LightOpenglActivity extends Activity {

    GLSurfaceView glView;
    LightRender lightRender;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        initBitmap.init(this.getResources());
        //bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall) ;
        lightRender = new LightRender();
        glView = new GLSurfaceView(this);
        glView.setRenderer(lightRender);

        setContentView(glView);


    }



    // 处理事件

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        lightRender.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

}
