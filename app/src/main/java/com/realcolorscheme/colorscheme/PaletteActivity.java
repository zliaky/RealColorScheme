package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zliaky on 2017/4/13.
 */
public class PaletteActivity extends Activity {

    private ColorPicker colorPicker;
    private Button homeBtn;
    private Button[] colorBtn;
    private Button colorShowBtn;
    Integer[] colorBtnId = new Integer[] {
            R.id.colorBtn0,R.id.colorBtn1,R.id.colorBtn2,R.id.colorBtn3,
            R.id.colorBtn4,R.id.colorBtn5,R.id.colorBtn6,R.id.colorBtn7,
            R.id.colorBtn8,R.id.colorBtn9,R.id.colorBtn10,R.id.colorBtn11};
    private int colorList[];

    private EditText[] editText;
    Integer[] editTextId = new Integer[] {
            R.id.colorR, R.id.colorG, R.id.colorB,
            R.id.colorC, R.id.colorM, R.id.colorY, R.id.colorK};

    private void setBtnBackground(Button button, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        float radii = (float) 20.0;
        drawable.setCornerRadii(new float[] { radii, radii, radii, radii,
                radii, radii, radii, radii });
        drawable.setStroke(1, Color.BLACK);
        drawable.setColor(color);
        button.setBackgroundDrawable(drawable);
    }

    public void setColorShowBtn(int color) {
        setBtnBackground(colorShowBtn, color);
    }

    private void colorRefresh(int color) {
        for (int i = 10; i >= 0; i--) {
            colorList[i+1] = colorList[i];
        }
        colorList[0] = color;
        for (int i = 0; i < 12; i++) {
            setBtnBackground(colorBtn[i], colorList[i]);
        }
        setColorText(color);
        setColorShowBtn(color);
    }

    private int[] rgb2cmyk(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        float black = Math.min(Math.min(255 - red, 255 - green), 255 - blue);
        float cyan, magenta, yellow;
        if (black!=255) {
            cyan = 100*(255-red-black)/(255-black);
            magenta = 100*(255-green-black)/(255-black);
            yellow = 100*(255-blue-black)/(255-black);
        } else {
            cyan = 255 - red;
            magenta = 255 - green;
            yellow = 255 - blue;
        }
        return new int[] {(int)cyan, (int)magenta, (int)yellow, (int)black};
    }

    public void setColorText(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int[] cmyk = rgb2cmyk(color);
        editText[0].setText(""+red);
        editText[1].setText(""+green);
        editText[2].setText(""+blue);
        editText[3].setText(""+cmyk[0]);
        editText[4].setText(""+cmyk[1]);
        editText[5].setText(""+cmyk[2]);
        editText[6].setText(""+cmyk[3]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        colorList = new int[12];
        for (int i = 0; i < 12; i++) {
            colorList[i] = Color.parseColor("#ffffff");
        }

        colorPicker = (ColorPicker)findViewById(R.id.colorPicker);
        colorPicker.setPalette(this);

        colorShowBtn = (Button)findViewById(R.id.showButton);

        homeBtn = (Button)findViewById(R.id.paletteToHome);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PaletteActivity.this, MainActivity.class);
                PaletteActivity.this.startActivity(intent);
            }
        });

        colorBtn = new Button[12];
        for (int i = 0; i < 12; i++) {
            colorBtn[i] = (Button)findViewById(colorBtnId[i]);
            colorBtn[i].setTag(i);
            colorBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button)view;
                    int num = (int)button.getTag();
                    colorRefresh(colorList[num]);
                }
            });
        }

        editText = new EditText[7];
        for (int i = 0; i < 7; i++) {
            editText[i] = (EditText)findViewById(editTextId[i]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int color = colorPicker.getColor();
            colorRefresh(color);
        }
        return super.onTouchEvent(event);
    }
}
