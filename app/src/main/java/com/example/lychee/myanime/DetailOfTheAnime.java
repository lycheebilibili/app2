package com.example.lychee.myanime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class DetailOfTheAnime extends AppCompatActivity {
   public String theid;
   public String fileName = "collectID";
   public TextView savedFile;
   public EditText uiEditText;
    public ArrayList<String> loveCollection;
    public ImageView theLove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_of_the_anime);




        TextView theMainName = (TextView) findViewById(R.id.main_name);
        TextView theMainSummary = (TextView) findViewById(R.id.main_summary);
        ImageView theMainImage = (ImageView) findViewById(R.id.main_image);


        //获取Intent和数据
        Intent intent = getIntent();
        //获取Intent中绑定的Bundle
        Bundle result = intent.getExtras();
        theMainName.setText(result.getString("name"));
        theid = result.getString("id");

        theMainSummary.setText(result.getString("summary"));
        byte[] bitmapArray = result.getByteArray("bitmap");
        Bitmap tempBitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        theMainImage.setImageBitmap(tempBitmap);

        //判断有没有被收藏进数组,设置初始红心
        theLove = (ImageView)findViewById(R.id.heart);
        loveCollection = readArray();
        if (loveCollection.contains(theid)){theLove.setImageResource(R.drawable.baseline_favorite_white_18dp);}
        else {theLove.setImageResource(R.drawable.baseline_favorite_border_white_18dp);}



        //设置文本编辑器和影评栏初始状态
        Button saveOrEdit = (Button)findViewById(R.id.save_or_edit);
        savedFile = (TextView)findViewById(R.id.textContent);
         uiEditText = (EditText)findViewById(R.id.editContent);
      if(load().isEmpty()){uiEditText.setVisibility(View.VISIBLE);
      savedFile.setVisibility(View.GONE);}
      else {savedFile.setVisibility(View.VISIBLE);
      savedFile.setText(load());
      uiEditText.setVisibility(View.GONE);}


        //储存文本
         saveOrEdit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String convertFile;
                if (savedFile.getVisibility()==View.GONE){
                    convertFile = uiEditText.getText().toString();
                    save(convertFile);
                savedFile.setText(load());
                uiEditText.setVisibility(View.GONE);
                savedFile.setVisibility(View.VISIBLE);}
                else {convertFile = load();
                uiEditText.setText(convertFile);
                uiEditText.setVisibility(View.VISIBLE);
                savedFile.setVisibility(View.GONE);}
             }

         });

         //点赞小心心

        theLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loveCollection.contains(theid)) {
                    loveCollection.add(theid);
                theLove.setImageResource(R.drawable.baseline_favorite_white_18dp);
                saveArray(loveCollection);}
                else {loveCollection.remove(theid);
                theLove.setImageResource(R.drawable.baseline_favorite_border_white_18dp);
                saveArray(loveCollection);}
            }
        });

    }
    //储存文本函数
    public void save(String data) {
        FileOutputStream fos;
       try{ fos = openFileOutput(fileName+theid, Context.MODE_PRIVATE);
        fos.write(data.getBytes());
        fos.close();}
        catch (Exception e){e.printStackTrace();}
    }
    //读取文本函数
    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(fileName+theid);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = " ";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }


    //储存数组函数
    public boolean saveArray(ArrayList<String> lovecollection) {
        SharedPreferences.Editor editor = getSharedPreferences("AnimeLoveCollectionFile", MODE_PRIVATE).edit();
        editor.putInt("LoveNums", lovecollection.size());
        for (int i = 0; i < lovecollection.size(); i++)
        {
            editor.putString("item_"+i, lovecollection.get(i));
        }
        return editor.commit();
    }

    //读取数组函数
    public ArrayList<String> readArray() {
        ArrayList<String> lovecollection =new ArrayList<String>();
        SharedPreferences preferDataList = getSharedPreferences("AnimeLoveCollectionFile", MODE_PRIVATE);
        int LoveNums = preferDataList.getInt("LoveNums", 0);
        for (int i = 0; i < LoveNums; i++) {
            String loveItem = preferDataList.getString("item_" + i, null);
            lovecollection.add(loveItem);
        }
        return lovecollection;
    }
}
