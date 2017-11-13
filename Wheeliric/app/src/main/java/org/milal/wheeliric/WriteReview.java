package org.milal.wheeliric;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by ekgml on 2017-08-21.
 */

public class WriteReview extends AppCompatActivity{

    Button cameraBtn = null;
    ImageView iv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        setCamera();
    }

    private void setCamera(){
        cameraBtn = (Button)findViewById((R.id.cameraBtn));
        iv = (ImageView)findViewById(R.id.iv);

        cameraBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivity(cameraIntent)
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        iv.setImageURI(data.getData());
    }
}
