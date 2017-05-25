package com.realcolorscheme.colorscheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zliaky on 2017/4/13.
 */
public class SchemeActivity extends Activity {

    private static final int BUFFER_SIZE = 8192;
    private GridView gridView;
    private Button homeBtn;
    private Button addBtn;
    private List<Bitmap> bitmapList;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);
        bitmapList = new ArrayList<Bitmap>();

        File path = new File(Global.imgPath);
        File[] files = path.listFiles();

        for (int i = 0; i < files.length; i++) {
            try {
                Bitmap tempBitmap = getImageDrawable(files[i].getAbsolutePath());
                bitmapList.add(tempBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gridView = (GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this, bitmapList);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Global.bitmap = bitmapList.get(position);
                Intent intent = new Intent();
                intent.setClass(SchemeActivity.this, RecommendActivity.class);
                SchemeActivity.this.startActivity(intent);
            }
        });
/*

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SchemeActivity.this);
                builder.setTitle("删除");
                builder.setMessage("确认删除收藏吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.out.println("1");
                        bitmapList.remove(position);
                        System.out.println("2");

                        imageAdapter.setBitmapList(bitmapList);
                        Toast.makeText(SchemeActivity.this, position + " " + bitmapList.size() + " "
                                        + " " + imageAdapter.getBitmapListSize(),
                                Toast.LENGTH_SHORT).show();
                        System.out.println("3");
                        imageAdapter.notifyDataSetChanged();
//                        Toast.makeText(SchemeActivity.this, position + " " + imageAdapter.getBitmapListSize(),
//                                Toast.LENGTH_SHORT).show();
                        System.out.println("4");
                        File file = new File(Global.imgPath + "/" + Global.filenameList.get(position));
                        System.out.println("5");
                        if (file.exists() && file.isFile()) {
                            System.out.println("exist");
//                            file.delete();
                        }
                        System.out.println("6");
//                        Global.filenameList.remove(position);

                        Toast.makeText(SchemeActivity.this, "Delete succeed!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
*/
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
