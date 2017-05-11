package com.example.kosta.musicandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.musicandroid.domain.Music;

public class MusicDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        TextView musicId = (TextView)findViewById(R.id.musicDetailId);
        TextView name = (TextView)findViewById(R.id.nameDetail);
        TextView artist = (TextView)findViewById(R.id.artistDetail);
        TextView album = (TextView)findViewById(R.id.albumDetail);
        TextView agent = (TextView)findViewById(R.id.agentDetail);
        ImageView image = (ImageView)findViewById(R.id.imageDetail);

        Music music = new Music();

        new DetailLoadingTask(music).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobiledetail.do?id=" + getIntent().getExtras().get("id"));

        musicId.setText(music.getId() + "");
        name.setText(music.getName());
        artist.setText(music.getArtist());
        album.setText(music.getAlbum());
        agent.setText(music.getAgent());

        new ImageLoadingTask(image).execute(music.getImage());
    }
}
