package com.example.lychee.myanime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class AnimeConnection {
    public static final String LOG_TAG = AnimeConnection.class.getSimpleName();

    public static String comedyPart = "https://kitsu.io/api/edge/anime?filter[genres]=comedy";
    public static String magicPart = "https://kitsu.io/api/edge/anime?filter[genres]=magic";
    public static String mysteryPart = "https://kitsu.io/api/edge/anime?filter[genres]=mystery";
    public static String adventurePart = "https://kitsu.io/api/edge/anime?filter[genres]=adventure";
    public AnimeConnection() { }

    public static ArrayList<AnimeAbstract> totalConnection(String urlOfString) {
        URL url = creatURL(urlOfString);
        Log.w(LOG_TAG,"执行创建url操作：createUrl");
        String jsonResponse = null;
       try {
           jsonResponse = getTheConnection(url);
           Log.w(LOG_TAG,"执行了联网返回JSON操作：getTheConnection（url）");
       }
       catch (IOException e){Log.e(LOG_TAG,"wrong:Instream.close()");}
        ArrayList<AnimeAbstract> animeList = extractInfo(jsonResponse);
       Log.w(LOG_TAG,"执行了解析JSON返回可用数据操作extractInfo");
        return animeList;
    }

    private static URL creatURL(String str) {
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "creatURL();");
            return null;
        }
        return url;
    }

    public static String getTheConnection(URL url) throws IOException {

        String jsonResponse = "";
        InputStream is = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            Log.w(LOG_TAG,"已经执行httpURLConnection = (HttpURLConnection) url.openConnection()");
            httpURLConnection.setRequestMethod("GET");
            Log.w(LOG_TAG,"已经执行httpURLConnection.setRequestMethod(\"GET\");");
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setReadTimeout(15000);
            Log.w(LOG_TAG,"已经执行hhttpURLConnection.setConnectTimeout(20000);\n" +
                    "            httpURLConnection.setReadTimeout(15000);");
            httpURLConnection.connect();
            Log.w(LOG_TAG,"已经执行httpURLConnection.connect();");
            if (httpURLConnection.getResponseCode() == 200) {
                is = httpURLConnection.getInputStream();
                Log.w(LOG_TAG,"已经执行is = httpURLConnection.getInputStream();,准备readFromStream");
                jsonResponse = readFromStream(is);
                Log.w(LOG_TAG,"已经执行readFromStream");
            } else {
                Log.e(LOG_TAG, "" + httpURLConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "connection wrong!");
            return null;
        } finally {
            if (httpURLConnection != null) {httpURLConnection.disconnect(); }
            if(is!=null){is.close();}
        }
        return jsonResponse;
    }
    public static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line=bufferedReader.readLine();
        Log.w(LOG_TAG,"开始执行line = bufferedReader.readLine();");
        if (line!=null){
            stringBuilder.append(line);
        line = bufferedReader.readLine();}
        Log.w(LOG_TAG,"已经执行line = bufferedReader.readLine();");
        return stringBuilder.toString();
    }

    public static ArrayList<AnimeAbstract> extractInfo(String str){
        Log.w(LOG_TAG,"开始提取信息extractInfo");
        ArrayList<AnimeAbstract> abstractArrayList = new ArrayList<AnimeAbstract>();
        try{JSONObject original = new JSONObject(str);
            JSONArray dataArray = original.getJSONArray("data");
            for(int i = 0;i<dataArray.length();i++){
                JSONObject eachAnime = dataArray.getJSONObject(i);
                String theId = eachAnime.getString("id");
                JSONObject attribute = eachAnime.getJSONObject("attributes");
                String summary = attribute.getString("synopsis");
                JSONObject theTitle = attribute.getJSONObject("titles");
                String name = getTheTitle(theTitle);
                JSONObject tinyImage = attribute.getJSONObject("posterImage");
                String tinyImageUrl = tinyImage.getString("tiny");
                Log.w(LOG_TAG,"已经提取了name和summary，准备提取图片");
                Bitmap bitmapTinyImage = getBitmapImage(tinyImageUrl);
                Log.w(LOG_TAG,"已经提取图片");
                AnimeAbstract anime = new AnimeAbstract(bitmapTinyImage,name,summary,theId);
                abstractArrayList.add(anime);
                Log.w(LOG_TAG,"建立AnimeAbstract，增加ArrayList中的AnimeAbstsract");
            }
        }
        catch (JSONException e){Log.e(LOG_TAG,"wrong json"); }
        return abstractArrayList;
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

