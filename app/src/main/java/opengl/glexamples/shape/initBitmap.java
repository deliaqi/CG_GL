package opengl.glexamples.shape;

/**
 * Created by deliaqi on 2015/12/26.
 */
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import opengl.glexamples.R;

public class initBitmap {

    public static Bitmap bitmap;

    public static void init(Resources res){
        bitmap = BitmapFactory.decodeResource(res, R.drawable.wall) ;
    }
}

