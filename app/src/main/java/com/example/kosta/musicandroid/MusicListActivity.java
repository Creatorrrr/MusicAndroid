package com.example.kosta.musicandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.kosta.musicandroid.domain.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {

//    private static final int ALL_LIST = 0;
//    private static final int MY_LIST = 1;

    private List<Music> musics;
    private MusicAdapter adapter;

    private ToggleButton listChangeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        musics = new ArrayList<>();
        adapter = new MusicAdapter(this);

        listChangeBtn = (ToggleButton)findViewById(R.id.listToggle);
        listChangeBtn.setChecked(false);

        ListView list = (ListView)findViewById(R.id.list);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = musics.get(position);
                Intent intent = new Intent(MusicListActivity.this, MusicDetailActivity.class);
                intent.putExtra("music", music);
                startActivity(intent);
            }
        });

        final MusicLoadingTask task = new MusicLoadingTask(adapter, musics);
        task.execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilelist.do");
//        listType = ALL_LIST;

        final SharedPreferences prefs = getSharedPreferences("login_info", MODE_PRIVATE);

        listChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
                if(listChangeBtn.isChecked()) {
                    if(task.isCancelled()) {
                        String id = prefs.getString("id", "");
                        musics.clear();
                        new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilefavoritelist.do?loginId=" + id);
                    }
                } else {
                    if(task.isCancelled()) {
                        musics.clear();
                        new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilelist.do");
                    }
                }
            }
        });

//        findViewById(R.id.allBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilelist.do");
//                listType = ALL_LIST;
//            }
//        });
//
//        findViewById(R.id.myBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String id = getSharedPreferences("login_info", Context.MODE_PRIVATE).getString("id", "");
//                if(!id.isEmpty()) {
//                    new MusicLoadingTask(adapter, musics).execute("http://10.0.2.2:8080/MusicPlay_Spring/mobilefavoritelist.do?loginId=" + id);
//                    listType = MY_LIST;
//                } else {
//                    Intent intent = new Intent(MusicListActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        });

        registerForContextMenu(list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(listChangeBtn.isChecked()) {
            menu.add("삭제");
        } else {
            menu.add("담기");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String loginId = getSharedPreferences("login_info", Context.MODE_PRIVATE).getString("id", "");
        long musicId = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).id;

        if (listChangeBtn.isChecked()) {
            new MusicFavoriteTask(this)
                    .execute("http://10.0.2.2:8080/MusicPlay_Spring/mobiledeletefavorite.do" +
                            "?loginId=" + loginId +
                            "&musicId=" + musicId);Log.d("music", "delete");
        } else {
            new MusicFavoriteTask(this)
                    .execute("http://10.0.2.2:8080/MusicPlay_Spring/mobileaddfavorite.do" +
                            "?loginId=" + loginId +
                            "&musicId=" + musicId);Log.d("music", "add");
        }

        return true;
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
