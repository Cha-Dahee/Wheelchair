package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Yoojung on 2017-08-04.
 */

public class Tensorflow extends AsyncTask<String[], Void, ImageGridAdapter>{

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";

    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";

    private static final int ITEM_NUM = 6;
    private static final int ROW_NUM = 3;

    private static final String[] words = {
            "tobacco shop",
            "restaurant",
            "cinema",
            "bakery",
            "bookshop",
            "barbershop",
            "toyshop",
            "grocery store",
            "sliding door",
            "patio",
            "shoe shop",
            "library",
            "turnstile",
            "prison",
            "shoji",
            "mobile home",
            "monastery",
            "palace",
            "butcher shop",
            "church",
            "greenhouse",
            "window screen",
    };

    private Context context;
    private Classifier classifier;
    private GridView gridView;
    private TextView textView;
    private ImageGridAdapter imageGridAdapter;
    private ProgressDialog progressDialog;

    private String[] images = new String[100];
    private String[] urls = new String[100];
    //private String[] description = new String[ITEM_NUM];
    private Bitmap[] bitmaps = new Bitmap[ITEM_NUM];
    private String[] webs = new String[ITEM_NUM];
    private int position = 0;
    private int total = 0;

    public Tensorflow(Context context, GridView gridView, TextView textView){
        this.context = context;
        this.gridView = gridView;
        this.textView = textView;
        progressDialog = new ProgressDialog(context);
        imageGridAdapter = null;
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ImageGridAdapter result) {
        progressDialog.dismiss();

        if(bitmaps[0] == null) {
            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT);
            return ;
        }

        for(int i = 0; i < ITEM_NUM; i++) {
            if((i + 1) < ITEM_NUM && bitmaps[i+1] == null){
                Bitmap[] bTemp = new Bitmap[i+1];
                String[] wTemp = new String[i+1];

                for(int j = 0; j < i+1; j++){
                    bTemp[j] = bitmaps[j];
                    wTemp[j] = webs[j];
                }

                result = new ImageGridAdapter(context, bTemp, wTemp);
                result.notifyDataSetChanged();
                gridView.setVisibility(View.VISIBLE);
                gridView.invalidate();
                gridView.setAdapter(result);
                int height = (i+1) / ROW_NUM;

                if(((i+1) % ROW_NUM) != 0)
                    height++;

                ViewGroup.LayoutParams params = gridView.getLayoutParams();
                params.height = (INPUT_SIZE + 50) * height;
                gridView.setLayoutParams(params);
                gridView.requestLayout();

                textView.setText(Integer.toString(++total));

                return ;
            }
        }

        result = new ImageGridAdapter(context, bitmaps, webs);
        result.notifyDataSetChanged();
        gridView.setVisibility(View.VISIBLE);
        gridView.invalidate();
        gridView.setAdapter(result);
        int height = ITEM_NUM / ROW_NUM;

        if((ITEM_NUM % ROW_NUM) != 0)
            height++;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = (INPUT_SIZE + 50) * height;
        gridView.setLayoutParams(params);
        gridView.requestLayout();

        textView.setText(Integer.toString(++total));

        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(ImageGridAdapter result){
        if(progressDialog != null)
            progressDialog.dismiss();

        super.onCancelled();
    }

    @Override
    protected ImageGridAdapter doInBackground(String[]... params) {
        images = params[0];
        urls = params[1];

        initTensorFlowAndLoadModel();
        input_url();

        return imageGridAdapter;
    }

    private void initTensorFlowAndLoadModel() {
        try {
            classifier = TensorFlowImageClassifier.create(
                    context.getAssets(),
                    MODEL_FILE,
                    LABEL_FILE,
                    INPUT_SIZE,
                    IMAGE_MEAN,
                    IMAGE_STD,
                    INPUT_NAME,
                    OUTPUT_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int input_url(){
        int current = Integer.parseInt(textView.getText().toString());
        int iteration = ITEM_NUM + current;
        for (int i = current; i < iteration && i < 100; i++) {
            String image = images[i];
            total = i;
            try{
                InputStream input = new java.net.URL(image).openStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(input);
                final String url = urls[i];

                Boolean condition = recognize_bitmap(bitmap, url);

                if(!condition)
                    iteration++;
            } catch (NullPointerException e) {
                iteration++;
            } catch (FileNotFoundException e){
                iteration++;
            } catch (IOException e){
                iteration++;
            }
        }

        return total;
    }

    private Boolean recognize_bitmap(Bitmap bitmap, String url) {
        Bitmap temp = bitmap;
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

        for(int i = 0; i < words.length; i++) {
            if (results.toString().contains(words[i])) {
                //description[position] = results.toString();
                bitmaps[position] = temp;
                webs[position] = url;
                position++;
                return true;
            }
        }
        return false;
    }
}
