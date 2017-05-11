package com.example.kosta.musicandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kosta.musicandroid.domain.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {

    private List<Music> musics;
    private MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        musics = new ArrayList<>();
        adapter = new MusicAdapter(this);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilelist.do");

        findViewById(R.id.allBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilelist.do");
            }
        });

        findViewById(R.id.myBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getSharedPreferences("login_info", Context.MODE_PRIVATE).getString("id", "");
                if(!id.isEmpty()) {
                    new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilefavoritelist.do?loginId=" + id);
                } else {
                    Intent intent = new Intent(MusicListActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class MusicAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MusicAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return musics.size();
        }

        @Override
        public Object getItem(int position) {
            return musics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return musics.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.music_list_item, null);
            }

            TextView musicId = (TextView)convertView.findViewById(R.id.musicId);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView artist = (TextView)convertView.findViewById(R.id.artist);
            TextView album = (TextView)convertView.findViewById(R.id.album);
            ImageView image = (ImageView)convertView.findViewById(R.id.image);

            musicId.setText(musics.get(position).getId() + "");
            name.setText(musics.get(position).getName());
            artist.setText(musics.get(position).getArtist());
            album.setText(musics.get(position).getAlbum());

            new ImageLoadingTask(image).execute(musics.get(position).getImage());

            return convertView;
        }
    }
}
