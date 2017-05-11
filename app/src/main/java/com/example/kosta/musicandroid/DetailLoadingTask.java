package com.example.kosta.musicandroid;

import android.os.AsyncTask;

import com.example.kosta.musicandroid.domain.Music;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kosta on 2017-05-11.
 */

public class DetailLoadingTask extends AsyncTask<String, Void, Void> {

    private Music music;

    public DetailLoadingTask(Music music) {
        this.music = music;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(url.openStream()));

            NodeList nodeList = document.getElementsByTagName("music");
            Element element = (Element)nodeList.item(0);

            music.setId(Integer.parseInt(getTagValue("id", element)));
            music.setName(getTagValue("name", element));
            music.setAlbum(getTagValue("album", element));
            music.setArtist(getTagValue("artist", element));
            music.setImage("http://10.0.2.2:8080/MusicPlay_Spring/resources/img/" + getTagValue("image", element));
            music.setAgent(getTagValue("agent", element));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getTagValue(String tag, Element element) {
        if(element.getElementsByTagName(tag).item(0) == null) {
            return null;
        }
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }
}
