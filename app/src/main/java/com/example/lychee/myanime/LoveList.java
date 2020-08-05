package com.example.lychee.myanime;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LoveList extends AppCompatActivity implements LoaderManager.LoaderCallbacks{



    public static ArrayList<String> loveListObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_list);
        loveListObject = readArray();
        Loader loader = getSupportLoaderManager().initLoader(1,null,this);
        loader.forceLoad();


    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new LoveLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        final AnimeAdapter adapter = new AnimeAdapter(this,(ArrayList<AnimeAbstract>) data);
        ListView theListView = (ListView) findViewById(R.id.the_list_id);
        theListView.setAdapter(adapter);
        TextView text = (TextView)findViewById(R.id.waitingLoading_two);
        text.setVisibility(View.INVISIBLE);


        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimeAbstract animeOfClick = adapter.getItem(position);
                // 一个Bundle 对象
                Bundle data=new Bundle();
                //向Bundle中绑定数据，以键值对的形式
                data.putString("name", animeOfClick.getmName());
                data.putString("summary", animeOfClick.getmSummary());
                //bitmap转成byte再向bundle传递
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                animeOfClick.getmBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                data.putByteArray("bitmap",baos.toByteArray());
                data.putString("id",animeOfClick.getmID());

                Intent intent=new Intent(LoveList.this,DetailOfTheAnime.class);
                //把Bundle绑定到Intent中
                intent.putExtras(data);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

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
