package com.example.lychee.myanime;


import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.time.format.TextStyle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public AnimeAdapter comedyAdapter;
    public AnimeAdapter magicAdapter;
    public AnimeAdapter mysteryAdapter;
    public AnimeAdapter adventureAdapter;
    public TextView waiting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView adventure = (TextView)findViewById(R.id.adventure_part);
        final TextView magic = (TextView)findViewById(R.id.magic_part);
        final TextView mystery = (TextView)findViewById(R.id.mystery_part);
        final TextView comedy = (TextView)findViewById(R.id.comedy_part);
          waiting = findViewById(R.id.waitingLoading);
        //初始化Loader
        final android.support.v4.content.Loader loader1 = getSupportLoaderManager().initLoader(1,null,MainActivity.this);
        Log.w("MainActivity","初始化loader1");
        loader1.forceLoad();
        Log.w("MainActivity","这是执行万forceLoad");
        final android.support.v4.content.Loader loader2 = getSupportLoaderManager().initLoader(2,null,MainActivity.this);
        Log.w("MainActivity","初始化loader2");
        final android.support.v4.content.Loader loader3 = getSupportLoaderManager().initLoader(3,null,MainActivity.this);
        Log.w("MainActivity","初始化loader3");
        final android.support.v4.content.Loader loader4 = getSupportLoaderManager().initLoader(4,null,MainActivity.this);
        Log.w("MainActivity","初始化loader4");



        //冒险监听器
        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting.setVisibility(View.VISIBLE);
                adventure.setTypeface(adventure.getTypeface(),1|2);
               magic.setTypeface(magic.getTypeface(),2);
               comedy.setTypeface(comedy.getTypeface(),2);
               mystery.setTypeface(mystery.getTypeface(),2);
               loader1.forceLoad();

            }
        });
        //魔术的按钮监听器
        magic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting.setVisibility(View.VISIBLE);
                magic.setTypeface(magic.getTypeface(),1|2);
                adventure.setTypeface(adventure.getTypeface(),2);
                comedy.setTypeface(comedy.getTypeface(),2);
                mystery.setTypeface(mystery.getTypeface(),2);
                loader2.forceLoad();
            }
        });
        //喜剧监听器
        comedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting.setVisibility(View.VISIBLE);
                comedy.setTypeface(comedy.getTypeface(),1|2);
                magic.setTypeface(magic.getTypeface(),2);
                adventure.setTypeface(adventure.getTypeface(),2);
                mystery.setTypeface(mystery.getTypeface(),2);
                loader3.forceLoad();
            }
        });
        //神秘监听器
        mystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting.setVisibility(View.VISIBLE);
                mystery.setTypeface(mystery.getTypeface(),1|2);
                magic.setTypeface(magic.getTypeface(),2);
                comedy.setTypeface(comedy.getTypeface(),2);
                adventure.setTypeface(adventure.getTypeface(),2);
                loader4.forceLoad();
            }
        });

        //打开loveList
        ImageView loveList =(ImageView)findViewById(R.id.love_list);
        loveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoveList = new Intent();
                toLoveList.setClass(MainActivity.this,LoveList.class);
                startActivity(toLoveList);

            }
        });

    }

    @NonNull
    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, @Nullable Bundle args) {

        if(id == 1){Log.w("MainActivity","这个是onCreateLoader1");
        return new AdventureAbstractLoader(MainActivity.this);}

        else if(id==2) {Log.w("MainActivity","这个是onCreateLoader2");
                return new MagicAbstractLoader(MainActivity.this);}
        else if(id ==3){Log.w("MainActivity","这个是onCreateLoader3");
            return new ComedyAbstractLoader(MainActivity.this);}
        else if(id ==4){Log.w("MainActivity","这个是onCreateLoader4");
            return new MysteryAbstractLoader(MainActivity.this);}

        else {Log.e("MainActivity","Loader Wrong!");
        return null;}
    }

    @Override
    public void onLoadFinished(@NonNull final android.support.v4.content.Loader loader, Object data) {
        final AnimeAdapter adapter = new AnimeAdapter(this,(ArrayList<AnimeAbstract>) data);
        ListView theListView = (ListView) findViewById(R.id.the_list_id);
        theListView.setAdapter(adapter);
        waiting.setVisibility(View.INVISIBLE);
        Log.w("MainActivity","这个是onLoadFinished");
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

                Intent intent=new Intent(MainActivity.this,DetailOfTheAnime.class);
                //把Bundle绑定到Intent中
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader loader) {
                      adventureAdapter.setAnime(new ArrayList<AnimeAbstract>());
                      comedyAdapter.setAnime(new ArrayList<AnimeAbstract>());
                      magicAdapter.setAnime(new ArrayList<AnimeAbstract>());
                      mysteryAdapter.setAnime(new ArrayList<AnimeAbstract>());
                      Log.w("MainActivity","这个是reset");

    }




}
