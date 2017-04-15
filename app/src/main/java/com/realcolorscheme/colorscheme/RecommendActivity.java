package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by zliaky on 2017/4/15.
 */
public class RecommendActivity extends Activity {

    private ImageView imageView;
    private Button homeBtn;
//    private Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        imageView = (ImageView)findViewById(R.id.recImg);
        imageView.setImageBitmap(Global.bitmap);
    }
}
