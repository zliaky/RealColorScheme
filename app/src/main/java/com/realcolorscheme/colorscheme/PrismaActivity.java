package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by zliaky on 2017/4/13.
 */
public class PrismaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisma);

        ImageView imageView = (ImageView)findViewById(R.id.img_prisma);
        imageView.setImageBitmap(Global.bitmap);
    }

}
