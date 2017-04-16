package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zliaky on 2017/4/15.
 */
public class RecommendActivity extends Activity {

    private ImageView imageView;
    private Button homeBtn;
//    private Button
    private Cluster cluster;
    private Button[] recBtns = new Button[]{};
    private Button[] showBtns = new Button[]{};

    //Queue<Integer> fifo = new LinkedList<Integer>();
    private int fifo[] = {-1,-1,-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recommend);

        imageView = (ImageView)findViewById(R.id.recImg);
        imageView.setImageBitmap(Global.bitmap);

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





        recBtns[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[0].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });

        recBtns[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[1].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });
        recBtns[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[2].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });

        recBtns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[3].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });

        recBtns[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[4].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });

        recBtns[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable background = recBtns[5].getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                System.out.println("Color" + color);
                updateQueue(color);
            }
        });


        cluster = new Cluster();
        Bitmap bitmap = Global.bitmap;//BitmapFactory.decodeFile(Global.filename);
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
            }

        }
        else {
            System.out.println("bitmap null");
            //debug("bitmap null");
        }

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

}
