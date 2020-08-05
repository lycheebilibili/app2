package com.example.lychee.myanime;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends ArrayAdapter<AnimeAbstract> {
    public AnimeAdapter(Context context, ArrayList<AnimeAbstract> animeList){
        super(context,0,animeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if(currentItemView==null){
           currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_of_list,parent,false);
        }
        AnimeAbstract tempItem = getItem(position);
        //名字
        TextView name = (TextView)currentItemView.findViewById(R.id.animeName);
        name.setText(tempItem.getmName());
        //梗概
        TextView summary = (TextView) currentItemView.findViewById(R.id.animeSummary);
        summary.setText(tempItem.getmSummary());
        //图片
        ImageView image = (ImageView)currentItemView.findViewById(R.id.animeMidia);
        image.setImageBitmap(tempItem.getmBitmap());



        return currentItemView;
    }
    public void setAnime(List<AnimeAbstract> data) {
        addAll(data);
        notifyDataSetChanged();
    }

}
