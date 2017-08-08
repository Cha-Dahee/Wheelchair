package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;

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

    private static final int ITEM_NUM = 12;

    private Context context;
    private Classifier classifier;
    private GridView gridView;
    private ImageGridAdapter imageGridAdapter;
    private ProgressDialog progressDialog;

    private String[] images = new String[100];
    private String[] urls = new String[100];
    private String[] description = new String[ITEM_NUM];
    private Bitmap[] bitmaps = new Bitmap[ITEM_NUM];
    private String[] webs = new String[ITEM_NUM];
    private int position = 0;

    public Tensorflow(Context context, GridView gridView){
        this.context = context;
        this.gridView = gridView;
        progressDialog = new ProgressDialog(context);
        imageGridAdapter = null;
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ImageGridAdapter result) {
        progressDialog.dismiss();
        result = new ImageGridAdapter(context, description, bitmaps, webs);
        result.notifyDataSetChanged();
        gridView.invalidate();
        gridView.setAdapter(result);

        super.onPostExecute(result);
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

    private void input_url(){
        int iteration = ITEM_NUM;
        for (int i = 0; i < iteration; i++) {
            String image = images[i];

            try{
                InputStream input = new java.net.URL(image).openStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(input);
                final String url = urls[i];
                recognize_bitmap(bitmap, url);
            } catch (NullPointerException e) {
                iteration++;
            } catch (FileNotFoundException e){
                iteration++;
            } catch (IOException e){
                iteration++;
            }
        }
    }

    private void recognize_bitmap(Bitmap bitmap, String url) {
        bitmaps[position] = bitmap;
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

        description[position] = results.toString();
        webs[position] = url;
        position++;
    }
}
