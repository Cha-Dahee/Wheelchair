package com.example.lg.searchtest;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import java.util.logging.Handler;

public class Parsing extends XMLParser implements Runnable{
    private ArrayList<BusStationDatas> mDataList;
    private Handler mHandler;

    public Parsing(String addr, Handler handler) {
        super(addr);
        mHandler = handler;
    }
    public void startParsing() {
        XmlPullParser parser = getXMLParser("utf-8");

        if(parser == null){
            mDataList = null;
           // Log.d("BusStationXMLParser", "Paser Object is null");

        }else{  //parser가 null이 아니면, 즉 utf-8을 뭔가 읽어온다면!
            mDataList = new ArrayList<BusStationDatas>();   //BusStationData 객체 생성
            String busStationCode = null, busStationName = null,
                    busStationLati = null, busStationLongi = null; //data에서 뽑아낼 Bus내용
            String tag;

            try{
                int parserEvent = parser.getEventType();//parser의 상태를 나타내는듯, END인지 처음인지 그런거
                int tagIdentifier = 0;

                while(parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_DOCUMENT:
                             break;
                        case XmlPullParser.END_DOCUMENT:
                             break;
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();

                            /**요기를 바꾸면 될듯!! link로!*/

                            if(tag.equals("STATIONID")){
                                tagIdentifier = 1;
                            }else if(tag.equals("STATIONNAME")){
                                 tagIdentifier = 2;
                            }else if(tag.equals("LATITUDE")){
                                 tagIdentifier = 3;
                            }else if(tag.equals("LONGITUDE")){
                                 tagIdentifier = 4;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            /**위에서 tag 정보를 가지고, tagIdentifier의 값을 바꿔준다.
                             * 그리고 그 tag 사이의 text를 읽어온다.
                             */
                            if(tagIdentifier == 1){
                                busStationCode = parser.getText().trim();
                            }else if(tagIdentifier == 2){
                                busStationName = parser.getText().trim();
                            }else if(tagIdentifier == 3){
                                busStationLati = parser.getText().trim();
                            }else if(tagIdentifier == 4){
                                busStationLongi = parser.getText().trim();

                                BusStationDatas data = new BusStationDatas(busStationCode,
                                        busStationName, busStationLati, busStationLongi);
                                mDataList.add(data);
                            }
                            tagIdentifier = 0;
                            break;
                        }
                    parserEvent = parser.next();
                }
            }catch(Exception e){
                //Log.d("BusStationXMLParser", e.getMessage());
            }
        }
        //Log.d("BusStationXMLResult", Integer.toString(mDataList.size()));
    }

    public ArrayList<BusStationDatas> getResult(){
        return mDataList;
    }

    public void run() {
        startParsing();
        mHandler.sendEmptyMessage(0);
    }
}
