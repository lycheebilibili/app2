package com.example.lychee.myanime;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lychee.myanime.AnimeConnection.getTheConnection;

public class LoveLoader extends AsyncTaskLoader {

    public static final String LOG_TAG = LoveList.class.getSimpleName();

    final static String webURL = "https://kitsu.io/api/edge/anime/";

    public LoveLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        ArrayList<String> arrayList=LoveList.loveListObject;
        ArrayList<AnimeAbstract> abstractArrayList = new ArrayList<AnimeAbstract>();
        for(int i = 0;i<arrayList.size();i++){
              String urlOfString = webURL+arrayList.get(i);
              AnimeAbstract addAnime = totalConection(urlOfString);
              abstractArrayList.add(addAnime);
        }
        return abstractArrayList;
    }
    public AnimeAbstract totalConection(String urlOfString){
        URL url = creatURL(urlOfString);
        Log.w(LOG_TAG,"执行创建url操作：createUrl");
        String jsonResponse = null;
        try {
            jsonResponse = getTheConnection(url);
            Log.w(LOG_TAG,"执行了联网返回JSON操作：getTheConnection（url）");
        }
        catch (IOException e){Log.e(LOG_TAG,"wrong:Instream.close()");}
        AnimeAbstract anime = extractInfo(jsonResponse);
        Log.w(LOG_TAG,"执行了解析JSON返回可用数据操作extractInfo");
        return anime;

    }

    public static URL creatURL(String str){
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "creatURL();");
            return null;
        }
        return url;
    }

     public AnimeAbstract extractInfo(String str){
         AnimeAbstract anime = null;
         try{JSONObject original = new JSONObject(str);
             JSONObject data = original.getJSONObject("data");
                 String theId = data.getString("id");
                 JSONObject attribute = data.getJSONObject("attributes");
                 String summary = attribute.getString("synopsis");
                 JSONObject theTitle = attribute.getJSONObject("titles");
                 String name = getTheTitle(theTitle);
                 JSONObject tinyImage = attribute.getJSONObject("posterImage");
                 String tinyImageUrl = tinyImage.getString("tiny");
                 Log.w(LOG_TAG,"已经提取了name和summary，准备提取图片");
                 Bitmap bitmapTinyImage = getBitmapImage(tinyImageUrl);
                 Log.w(LOG_TAG,"已经提取图片");
                 anime = new AnimeAbstract(bitmapTinyImage,name,summary,theId);

         }
         catch (JSONException e){Log.e(LOG_TAG,"wrong json"); }
         return anime;
     }


    public static String getTheTitle(JSONObject theTitle) {

        if (!theTitle.optString("en").isEmpty()) {
            return theTitle.optString("en");
        } else {
            return theTitle.optString("ja_jp");
        }
    }
    public static Bitmap getBitmapImage(String someting){
        URL urlImage ;
        Bitmap imageBitmap = null;
        try {
            urlImage = creatURL(someting);
            HttpURLConnection imageConnection = (HttpURLConnection)urlImage.openConnection();
            imageConnection.setReadTimeout(15000);
            imageConnection.setConnectTimeout(20000);
            imageConnection.setDoInput(true);
            imageConnection.connect();
            Log.w(LOG_TAG,"图片网络连接：imageConnection.connect();");
            InputStream inputStream = imageConnection.getInputStream();
            Log.w(LOG_TAG,"输入图片流，getInputStream");
            imageBitmap = BitmapFactory.decodeStream(inputStream);
            Log.w(LOG_TAG,"将inputStream转换成Bitmap");
            imageConnection.disconnect();
            inputStream.close();
        }
        catch (IOException e){Log.e(LOG_TAG,"wrong with load picture ");}
        return imageBitmap;
    }
}
