package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by zliaky on 2017/4/13.
 */
public class PaletteActivity extends Activity {

    private ColorPicker colorPicker;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        colorPicker = (ColorPicker)findViewById(R.id.colorPicker);

        homeBtn = (Button)findViewById(R.id.paletteToHome);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PaletteActivity.this, MainActivity.class);
                PaletteActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int color = colorPicker.getColor();
            Button btn0 = (Button)findViewById(R.id.colorBtn0);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            float radii = (float) 20.0;
            drawable.setCornerRadii(new float[] { radii, radii, radii, radii, radii, radii, radii,
                    radii });
            drawable.setStroke(1, Color.BLACK);
            drawable.setColor(color);
            btn0.setBackgroundDrawable(drawable);

        }
        return super.onTouchEvent(event);
    }



}
