package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by zliaky on 2017/4/13.
 */
public class SchemeActivity extends Activity {

    private static final int BUFFER_SIZE = 8192;
    private GridView gridView;
    private Button homeBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Global.bitmap = Global.bitmapList.get(position);
                Intent intent = new Intent();
                intent.setClass(SchemeActivity.this, RecommendActivity.class);
                SchemeActivity.this.startActivity(intent);
            }
        });

        homeBtn = (Button)findViewById(R.id.collectionToHome);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SchemeActivity.this, MainActivity.class);
                SchemeActivity.this.startActivity(intent);
            }
        });

        addBtn = (Button)findViewById(R.id.addCollection);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);//open gallery
            }
        });

        File path = new File(Global.imgPath);
        File[] files = path.listFiles();

        for (int i = 0; i < files.length; i++) {
            try {
                Bitmap tempBitmap = getImageDrawable(files[i].getAbsolutePath());
                Global.bitmapList.add(tempBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getImageDrawable(String path)
            throws IOException
    {
        //打开文件
        File file = new File(path);
        if(!file.exists())
        {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[BUFFER_SIZE];

        //得到文件的输入流
        InputStream in = new FileInputStream(file);

        //将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }

        //转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
//        BitmapDrawable bd = new BitmapDrawable(bitmap);

        return bitmap;
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

        if (requestCode == 0) {
            Intent intent = new Intent();
            intent.setClass(SchemeActivity.this, RecommendActivity.class);
            SchemeActivity.this.startActivity(intent);
        } else {;
        }

//        switch (resultCode) {
//            case Activity.RESULT_OK: {
//                Uri uri = data.getData();
//
//                final String scheme = uri.getScheme();
//                String filename = null;
//                if ( scheme == null )
//                    filename = uri.getPath();
//                else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
//                    filename = uri.getPath();
//                } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
//                    Cursor cursor = getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
//                    if ( null != cursor ) {
//                        if ( cursor.moveToFirst() ) {
//                            int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
//                            if ( index > -1 ) {
//                                filename= cursor.getString( index );
//                            }
//                        }
//                        cursor.close();
//                    }
//                }
//
//                Global.filename = filename;
//
//                Intent intent=new Intent(SchemeActivity.this,RecommendActivity.class);
//                startActivity(intent);
//                this.finish();
//            }
//            break;
//            case Activity.RESULT_CANCELED:// 取消
//                break;
//        }



    }

}
