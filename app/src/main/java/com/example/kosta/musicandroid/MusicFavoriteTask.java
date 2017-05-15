package com.example.kosta.musicandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kosta on 2017-05-12.
 */

public class MusicFavoriteTask extends AsyncTask<String, Void, String> {

    private Context context;

    public MusicFavoriteTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String checkStr = null;

        try {
            Log.d("music", "asdf" + params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            checkStr = reader.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checkStr;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("true")) {
            Toast.makeText(context, "add/delete success", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(context, "add/delete fail", Toast.LENGTH_SHORT);
        }
    }
}
