package com.blackboxstudios.rafael.rsspodcastplayer.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.blackboxstudios.rafael.rsspodcastplayer.utils.FeedResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rafael on 25/09/2015.
 */
public class RetrieveRssFeed extends AsyncTask<String, Void, String> {

    private FeedResponse listener;

    private ProgressDialog dialog;

    private Context mContext = null;


    public RetrieveRssFeed(FeedResponse listener,Context context){

        this.listener = listener;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        String url;
        if (params[0].contains("http://")){
            url = params[0];
        }else {
            url = "http://" + params[0];
        }



        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.connect();
            int status = conn.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    Log.d("BLOB", sb.toString());
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Log.e("URL Exception", ex.toString());
        } catch (IOException ex) {
            Log.e("IO Exception", ex.toString());
        }
        return null;
    }

    protected void onPostExecute(String result) {



        try {
            listener.processFinish(result);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            JSONObject obj = new JSONObject(result);

            String iv = obj.getString("ivHex");
            String payload = obj.getString("data");

            Log.d("IV", iv);
            Log.d("DATA", payload);

            String decrypted = decrypt(payload,iv);

            JSONArray data = new JSONArray(decrypted);
            Log.d("DECRYPTED JSON", data.toString());
            listener.processFinish(data);



        } catch (Exception e) {
            Log.e( "JSON", e.toString() ) ;
        }*/
    }
}
