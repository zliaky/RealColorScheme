package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by zliaky on 2017/4/13.
 */
public class ARActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        ImageView imageView = (ImageView)findViewById(R.id.img_ar);
        imageView.setImageBitmap(Global.bitmap);

    }

}
