package org.milal.wheeliric;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Yoojung on 2017-02-01.
 */

public class JsoupParser extends AsyncTask<String, Void, Facility>{


    final static String DAEGU_FOOD = "DAEGU_FOOD";
    final static String DAEGU_STAY = "DAEGU_STAY";
    final static String SEOUL = "SEOUL";
    final static String ULSAN = "ULSAN";
    final static String GYEONGGI = "GYEONGGI";
    final static String TOUR = "TOUR";
    final static String GWANGJOO = "GWANGJOO";
    final static String GWANGJOO_FOOD = "GWANGJOO_FOOD";

    private String URL = "";
    private String CODE = "";
    private String FACILITY = "";
    //private String ADDRESS = "";
    private Facility facility;

    @Override
    protected Facility doInBackground(String... values) {

        facility = new Facility();
        CODE = values[0];
        FACILITY = values[1];
        //ADDRESS = values[2];
        Bitmap[] bitmap = new Bitmap[1];
        String output = "";
        HttpURLConnection conn;
        InputStream in;
        Document doc;
        Elements titles, node;

        switch(CODE){
            case DAEGU_FOOD:
                //대구 음식점
                URL = "http://wheeltour.or.kr/board/list/400a02?page_option=1&page_keyword=" + FACILITY;

                try {
                    doc = Jsoup.connect(URL).get();

                    //검색 결과 유무 확인
                    titles = doc.select("tbody");
                    node = titles.select("td[height]");
                    output = node.first().text();

                    if(!output.equals("검색결과가 없습니다.")) {

                        //검색 링크
                        titles = doc.select("tr");
                        node = titles.select("a[href]");
                        output = node.first().attr("href");
                        output = output.substring(2);

                        URL = "http://wheeltour.or.kr/board" + output;
                        doc = Jsoup.connect(URL).get();

                        //메인 사진
                        titles = doc.select("div.main_image");
                        node = titles.select("img[src]");
                        String src = node.first().attr("src");
                        int i = src.indexOf("&width");
                        output = src.substring(0, i);
                        URL Murl = new URL("http://wheeltour.or.kr" + output);
                        conn = (HttpURLConnection) Murl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        in = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(in);
                        facility.setImage(bitmap[0]);

                        //기타 사진
                        titles = doc.select("div.image_list_view");
                        node = titles.select("a[href]");

                        for (Element e : node) {
                            output = e.attr("href");
                            URL Iurl = new URL("http://wheeltour.or.kr" + output);
                            conn = (HttpURLConnection) Iurl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            in = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(in);
                            facility.setImage(bitmap[0]);
                        }

                        //시설 정보
                        titles = doc.select("ul.list_blue");
                        node = titles.select("li");

                        output = "";
                        for (Element e : node) {
                            output += e.text();
                            output += "\n";
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case DAEGU_STAY:
                //대구 숙소
                URL = "http://wheeltour.or.kr/board/list/500a02?page_option=1&page_keyword=" + FACILITY;

                try {
                    doc = Jsoup.connect(URL).get();

                    //검색 결과 유무 확인
                    titles = doc.select("tbody");
                    node = titles.select("td[height]");
                    output = node.first().text();

                    if(!output.equals("검색결과가 없습니다.")) {

                        //검색 링크
                        titles = doc.select("tr");
                        node = titles.select("a[href]");
                        output = node.first().attr("href");
                        output = output.substring(2);

                        URL = "http://wheeltour.or.kr/board" + output;
                        doc = Jsoup.connect(URL).get();

                        //메인 사진
                        titles = doc.select("div.main_image");
                        node = titles.select("img[src]");
                        String src = node.first().attr("src");
                        int i = src.indexOf("&width");
                        output = src.substring(0, i);
                        URL Murl = new URL("http://wheeltour.or.kr" + output);
                        conn = (HttpURLConnection) Murl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        in = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(in);
                        facility.setImage(bitmap[0]);

                        //기타 사진
                        titles = doc.select("div.image_list_view");
                        node = titles.select("a[href]");

                        for (Element e : node) {
                            output = e.attr("href");
                            URL Iurl = new URL("http://wheeltour.or.kr" + output);
                            conn = (HttpURLConnection) Iurl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            in = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(in);
                            facility.setImage(bitmap[0]);
                        }

                        //시설 정보
                        titles = doc.select("ul.list_blue");
                        node = titles.select("li");

                        output = "";
                        for (Element e : node) {
                            output += e.text();
                            output += "\n";
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case SEOUL:
                //한국말 안됨;;
                //Space & Case Sensitive 조심;;
                URL = "http://disability.seoul.go.kr/amenity/search.jsp?searchWord=" + FACILITY;

                try {
                    doc = Jsoup.connect(URL).get();

                    //검색 결과 유무 확인
                    titles = doc.select("ul.travel-list");
                    if(titles.select("p.thum").isEmpty()) {
                        output = "검색결과가 없습니다.";
                    } else if(!output.equals("검색결과가 없습니다.")){

                        //검색 링크
                        titles = doc.select("p.thum");
                        node = titles.select("a[href]");
                        output = node.first().attr("href");

                        URL = "http://disability.seoul.go.kr" + output;
                        doc = Jsoup.connect(URL).get();

                        //시설 정보
                        titles = doc.select("ul.viewloc");
                        node = titles.select("li");

                        output = "주  소 : " + node.first().text() + "\n";
                        output += node.first().nextElementSibling().text() + "\n";

                        titles = doc.select("h4.secTtl");
                        output += titles.first().text() + "\n";

                        titles = doc.select("dl.accordion");
                        node = titles.first().children();

                        for(Element e : node){
                            if(e.tagName().equals("dd")) {
                                e.select("a").remove();
                                output += " : ";
                                output += e.text();
                                output += "\n";
                            } else {
                                output += e.text();
                            }
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ULSAN:
                //검색이 안됨;;
                //Text가 입력은 되나 검색 버튼을 누르지 않으면 검색 결과가 뜨지 않음.

                URL = "http://usbf.kr/sub_search.html?se_txt=" + FACILITY;
                break;

            case GYEONGGI:
                //검색이 안됨;;
                //Text가 입력은 되나 검색 버튼을 누르지 않으면 검색 결과가 뜨지 않음.
                URL = "http://www.infoggd.or.kr/home/ggFacility/doFacilityList1.act?searchValue=" + FACILITY;

                /*
                try {
                    doc = Jsoup.connect(url).get();

                    //검색 결과 유무 확인
                    titles = doc.select("tbody");
                    if(titles.isEmpty()) {
                        output = "검색결과가 없습니다.";
                    } else if(!output.equals("검색결과가 없습니다.")){

                        //검색 링크
                        node = titles.select("td");
                        String src = node.first().attr("onclick");
                        int i = src.indexOf("(");
                        int j = src.indexOf(",");

                        Log.d(src, "attr");
                        output = src.substring(i+1, j);

                        url = "http://www.infoggd.or.kr/home/ggFacility/doFacilityViewL.act?tsnum=" + output;
                        doc = Jsoup.connect(url).get();

                        //이미지
                        titles = doc.select("table.facility_info_table_01");
                        node = titles.select("img[src]");

                        for (Element e : node) {
                            output = e.attr("src");
                            URL Iurl = new URL(output);
                            conn = (HttpURLConnection) Iurl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            in = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(in);
                            facility.setImage(bitmap[0]);
                        }

                        //시설 정보
                        titles = doc.select("table.facility_info_table");
                        node = titles.select("tr");
                        node.first().remove();
                        node = titles.select("td");
                        node.last().remove();
                        node = titles.select("td");

                        output = "주  소 : " + node.first().text();
                        output += "\n";
                        output += "전화번호 : " + node.last().text();
                        output += "\n";

                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;

            case TOUR:
                URL = "http://korean.visitkorea.or.kr/kor/bz15/access/brfree/tp_clearing_tour.jsp?areaCode=&keyword=" + FACILITY + "&gotoPage=&cid=&type=";

                try {
                    doc = Jsoup.connect(URL).get();

                    //검색 결과 유무 확인
                    titles = doc.select("div.doc");
                    node = titles.select("div.whereWrap");
                    output = node.first().text();

                    if(output.equals("데이터가 없습니다.")){
                        output = "검색결과가 없습니다.";
                    } else {

                        //검색 링크
                        titles = node.select("li");
                        node = titles.select("a[href]");
                        output = node.first().attr("href");
                        output = output.replace("course", "tour");
                        Log.d(output, "output");

                        URL = "http://korean.visitkorea.or.kr/kor/bz15/access/brfree/" + output;

                        doc = Jsoup.connect(URL).get();

                        //이미지
                        titles = doc.select("div.thumbWrap").select("figure").select("div.grap").select("div.obj");
                        node = titles.select("img[src]");

                        for (Element e : node) {
                            output = e.attr("src");
                            Log.d(output, "src");
                            URL Iurl = new URL(output);
                            conn = (HttpURLConnection) Iurl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            in = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(in);
                            facility.setImage(bitmap[0]);
                        }

                        //시설 정보
                        titles = doc.select("figcaption");
                        node = titles.select("ul");

                        output = "";
                        for (Element e : node.select("li")) {
                            if(e.select("b").text().equals("위치")){
                                output += "주  소 : " + e.select("span").text();
                                output += "\n";
                                Log.d(output, "address");
                            } else if(e.select("b").text().equals("문의") || e.select("b").text().equals("문의/안내")){
                                output += "전화번호 : " + e.select("span").text();
                                output += "\n";
                            } else {
                                output += e.select("b").text() + " : ";
                                output += e.select("span").text();
                                output += "\n";
                            }
                        }

                        //장애인 시설 정보
                        titles = doc.select("div.cntBox");
                        titles.select("span.detail").remove();
                        output += titles.select("div.title").select("a[href=#group3]").text() + "\n";
                        Log.d(output, "title");

                        titles = doc.select("ul.ptList");
                        titles.select("em").remove();
                        titles = doc.select("div#group3").select("ul.ptList").select("li");
                        node = titles.select("ul").select("li");

                        for(Element e : node){
                            output += e.text();
                            output += "\n";
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case GWANGJOO_FOOD:
                //광주 음식점
                URL = "https://www.gwangju.go.kr/contentsView.do?menuId=gwangju0410042600";

                try {
                    doc = Jsoup.connect(URL).get();
                    titles = doc.select("tbody");
                    node = titles.select("tr");

                    Log.d("tbody", node.first().text());

                    for(Element e : node){
                        titles = e.select("td");
                        String compare = titles.get(1).text();

                        Log.d("compare", compare);
                        if(compare.contains(FACILITY)){
                            output = "주  소 : " + titles.get(2).text() + "\n";
                            output += "전화번호 : " + titles.get(3).text() + "\n";
                            output += "주 메뉴 : " + titles.get(4).text() + "\n";
                            output += "업  태 : " + titles.get(5).text() + "\n";
                            output += "장애인 이용가능 : " + titles.get(6).text();

                            if(titles.get(6).text().equals("○"))
                                output += "(장애인 혼자 이용 가능업소)\n";
                            else if(titles.get(6).text().equals("△"))
                                output += "(동반자 도움으로 이용이 가능한 업소 ※화장실 출입이 불가할 수 있음)\n";

                            break;
                        }
                        else
                        {
                            output = "검색결과가 없습니다.";
                            Log.d("output", output);
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case GWANGJOO :
                //접속 안됨;;
                try {
                    FACILITY = URLEncoder.encode(FACILITY, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                URL = "http://편한세상.org/ew_list.php?key=&sub_key=&sub_cate=&option=&sb=&sbmode=sbmode&setext=" + FACILITY;

                try {
                    doc = Jsoup.connect(URL).get();

                    titles = doc.select("div.container-fluid");
                    node = titles.get(2).select("td[colspan=11]");
                    output = node.first().text();
                    Log.d("output2td", output);

                    if(output.equals("등록된 내용이 없습니다.")) {
                        output = "검색결과가 없습니다.";
                    } else {
                        //검색 링크
                        titles = doc.select("div.row");
                        node = titles.select("div[style=margin-bottom: 15px;]");

                        titles = node.select("a[href]");
                        output = titles.first().attr("href");
                        output = output.substring(1);

                        Log.d("substring", output);

                        URL = "http://편한세상.org" + output;

                        doc = Jsoup.connect(URL).get();

                        //사진
                        titles = doc.select("ui#inner_list");
                        node = titles.select("a[href]");

                        for (Element e : node) {
                            output = e.attr("href");
                            output = output.substring(1);
                            URL Iurl = new URL("http://http://편한세상.org" + output);
                            conn = (HttpURLConnection) Iurl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            in = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(in);
                            facility.setImage(bitmap[0]);
                        }

                        //시설 정보
                        titles = doc.select("table.table.fontoption");
                        node = titles.select("tr");
                        node.first().remove();

                        output = "";
                        for (Element e : node) {
                            titles = e.select("h5");
                            output += titles.first().text() + " : ";
                            output += titles.last().text() + "\n";
                        }

                        titles = doc.select("div.col-md-12");
                        titles.first().remove();
                        titles.first().remove();
                        titles.last().remove();

                        node = titles.first().select("h2");
                        output += node.text() + " : ";
                        node = titles.first().select("strong");

                        for (Element e : node) {
                            output += e.text() + ", ";
                        }
                        output += "\n";

                        node = titles.last().select("h2");
                        output += node.text() + " : ";

                        titles = doc.select("div.col-md-4");
                        node = titles.select("tr");

                        for (Element e : node) {
                            titles = e.select("td");
                            titles.first().remove();

                            output += titles.first().text();
                            if (titles.last().select("img").attr("src").contains("ok"))
                                output += " ○";
                            else
                                output += " Χ";
                            output += "\n";
                        }
                    }
                    facility.setInfo(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        return facility;
    }
}
