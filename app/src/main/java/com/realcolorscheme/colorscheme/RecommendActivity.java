package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zliaky on 2017/4/15.
 */
public class RecommendActivity extends Activity {

    private ImageView imageView;
    private Button homeBtn;
    private Button printOut;
    private Button collectBtn;

    private Cluster cluster;
    private Button[] recBtns = new Button[]{};
    private Button[] showBtns = new Button[]{};

    private int[] recColor;

    //Queue<Integer> fifo = new LinkedList<Integer>();
    private int fifo[] = {-1,-1,-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recommend);

        imageView = (ImageView)findViewById(R.id.recImg);
        imageView.setImageBitmap(Global.bitmap);
        imageView.setOnTouchListener(imgSourceOnTouchListener);

        homeBtn = (Button)findViewById(R.id.recToHome);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RecommendActivity.this, MainActivity.class);
                RecommendActivity.this.startActivity(intent);
            }
        });

        recColor = new int[6];

        collectBtn = (Button)findViewById(R.id.recCollect);
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File img = new File(Global.imgPath + "/" + Global.filename);
                if (!img.exists()) {
                    try {
                        FileOutputStream out = new FileOutputStream(img);
                        Global.bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        Global.filenameList.add(Global.filename);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RecommendActivity.this, "Collect succeed!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecommendActivity.this, "Already colleced!",
                            Toast.LENGTH_SHORT).show();
                }


//              File file = new File("///mnt/sdcard/DCIM/RealColor");
            }
        });

        recBtns = new Button[]{
            (Button)findViewById(R.id.recBtn0),
            (Button)findViewById(R.id.recBtn1),
            (Button)findViewById(R.id.recBtn2),
            (Button)findViewById(R.id.recBtn3),
            (Button)findViewById(R.id.recBtn4),
            (Button)findViewById(R.id.recBtn5),
        };

        showBtns = new Button[]{
            (Button)findViewById(R.id.showBtn0),
            (Button)findViewById(R.id.showBtn1),
            (Button)findViewById(R.id.showBtn2),
            (Button)findViewById(R.id.showBtn3),
        };

        for(int i = 0; i < 6; i++) {
            recBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable background = ((Button)view).getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    int color = colorDrawable.getColor();
                    System.out.println("Color" + color);
                    updateQueue(color);
                }
            });
        }

        cluster = new Cluster();
        Bitmap bitmap = Global.bitmap;
        int seedNum = 6;
        int iterNum = 3;
        if(cluster.setImage(bitmap)) {
            //Log.d("bitmap","not null");
            //debug("bitmap not null");
            cluster.initSeed(seedNum);
            cluster.updateCluster();
            for(int i = 0 ; i < iterNum; i++) {
                cluster.updateSeed();
                cluster.updateCluster();
            }

            Node[] seeds = cluster.getSeeds();
            for(int i = 0; i < seedNum; i++) {
                recBtns[i].setBackgroundColor(Color.rgb(seeds[i].r,seeds[i].g,seeds[i].b));
                recColor[i] = Color.rgb(seeds[i].r,seeds[i].g,seeds[i].b);
            }

        }
        else {
            System.out.println("bitmap null");
            //debug("bitmap null");
        }

        printOut = (Button)findViewById(R.id.recPrint);
        printOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bluetooth.connectBt();
            }
        });



    }
    void updateQueue(int color) {
        for (int i = 3; i >= 0; i--) {
            fifo[i+1] = fifo[i];
        }
        fifo[0] = color;
        updateView();
    }

    void updateView() {
        for(int i = 0 ;i < 4; i++) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            float radii = (float) 20.0;
            drawable.setCornerRadii(new float[] { radii, radii, radii, radii,
                    radii, radii, radii, radii });
            drawable.setStroke(1, Color.BLACK);
            drawable.setColor(fifo[i]);
            showBtns[i].setBackgroundDrawable(drawable);
        }
    }


    View.OnTouchListener imgSourceOnTouchListener
            = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float eventX = event.getX();
                float eventY = event.getY();
                float[] eventXY = new float[] {eventX, eventY};

                Matrix invertMatrix = new Matrix();
                ((ImageView)view).getImageMatrix().invert(invertMatrix);

                invertMatrix.mapPoints(eventXY);
                int x = Integer.valueOf((int)eventXY[0]);
                int y = Integer.valueOf((int)eventXY[1]);

                boolean outside = false;
                //Limit x, y range within bitmap
                if(x < 0 || x > Global.bitmap.getWidth()-1 ||
                        y < 0|| y > Global.bitmap.getHeight()-1){
                    outside = true;
                }

                if (!outside) {
                    int touchedRGB = Global.bitmap.getPixel(x, y);
                    updateQueue(touchedRGB);
                }
            }

            return true;
        }
    };

}
