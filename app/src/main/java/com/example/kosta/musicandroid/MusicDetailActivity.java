package com.example.kosta.musicandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.musicandroid.domain.Music;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MusicDetailActivity extends AppCompatActivity {

    private Music music;

    private TextView musicId;
    private TextView name;
    private TextView artist;
    private TextView album;
    private TextView agent;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Music music = (Music)intent.getExtras().get("music");

        ((TextView)findViewById(R.id.detailNoText)).setText(music.getId() + "");
        ((TextView)findViewById(R.id.detailTitleText)).setText(music.getName());
        ((TextView)findViewById(R.id.detailArtistText)).setText(music.getArtist());
        ((TextView)findViewById(R.id.detailAlbumText)).setText(music.getAlbum());
        ((TextView)findViewById(R.id.detailAgentText)).setText(music.getAgent());

        ImageView img = (ImageView)findViewById(R.id.detailImage);
        new ImageLoadingTask(img).execute(music.getImage());

//        music = new Music();
//
//        musicId = (TextView)findViewById(R.id.musicDetailId);
//        name = (TextView)findViewById(R.id.nameDetail);
//        artist = (TextView)findViewById(R.id.artistDetail);
//        album = (TextView)findViewById(R.id.albumDetail);
//        agent = (TextView)findViewById(R.id.agentDetail);
//        image = (ImageView)findViewById(R.id.imageDetail);
//
//        new DetailLoadingTask().execute("http://10.0.2.2:8080/MusicPlay_Spring/mobiledetail.do?id=" + getIntent().getExtras().get("id"));
    }

//    private class DetailLoadingTask extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... params) {
//            try {
//                URL url = new URL(params[0]);
//
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document document = builder.parse(new InputSource(url.openStream()));
//
//                Element element = (Element)document.getElementsByTagName("music").item(0);
//
//                music.setId(Integer.parseInt(getTagValue("id", element)));
//                music.setName(getTagValue("name", element));
//                music.setAlbum(getTagValue("album", element));
//                music.setArtist(getTagValue("artist", element));
//                music.setImage("http://10.0.2.2:8080/MusicPlay_Spring/resources/img/" + getTagValue("image", element));
//                music.setAgent(getTagValue("agent", element));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (SAXException e) {
//                e.printStackTrace();
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            musicId.setText(music.getId() + "");
//            name.setText(music.getName());
//            artist.setText(music.getArtist());
//            album.setText(music.getAlbum());
//            agent.setText(music.getAgent());
//
//            new ImageLoadingTask(image).execute(music.getImage());
//        }
//
//        private String getTagValue(String tag, Element element) {
//            if(element == null) {
//                return null;
//            }
//            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
//            return nodeList.item(0).getNodeValue();
//        }
//    }
}
