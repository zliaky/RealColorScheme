package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button prismaBtn = null;
    private Button arBtn = null;
    private Button paletteBtn = null;
    private Button schemeBtn = null;

    ProgressDialog myDialog;

    private String filename = "/sdcard/temp.jpg";
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        prismaBtn = (Button)findViewById(R.id.Prisma);
        prismaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        arBtn = (Button)findViewById(R.id.AR);
        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        paletteBtn = (Button)findViewById(R.id.Palette);
        paletteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PaletteActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        schemeBtn = (Button)findViewById(R.id.Scheme);
        schemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SchemeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Global.imgPath = getApplicationContext().getFilesDir().getAbsolutePath()+"/img";
        File imgDir = new File(Global.imgPath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        Global.bitmapList = new ArrayList<Bitmap>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("requestCode:"+requestCode+",resultCode:"+resultCode+",data:"+data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Global.bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                List<String> segments = uri.getPathSegments();
                Global.filename = segments.get(segments.size() - 1);
            } catch (FileNotFoundException e) {
                System.out.println("can not found file");
            }
        }

        if (requestCode == 1) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PrismaActivity.class);
            MainActivity.this.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ARActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }
}
