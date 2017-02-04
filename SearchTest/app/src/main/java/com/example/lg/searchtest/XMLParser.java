package com.example.lg.searchtest;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

public abstract class XMLParser{
    private String mAddr;

    public XMLParser(String addr){
        mAddr = addr;
    }

    public XmlPullParser getXMLParser(String type){
        try{
            URL targetURL = new URL(mAddr);
            InputStream is = targetURL.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, type);
            return parser;
        }catch(Exception e){
           Log.d("XMLParser", e.getMessage());
            return null;
        }
    }

    public abstract void startParsing();
}
