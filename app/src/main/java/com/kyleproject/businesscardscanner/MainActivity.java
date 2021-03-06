package com.kyleproject.businesscardscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;
import static android.R.attr.text;


public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    static StringBuilder stringBuilder;
    TextRecognizer textRecognizer;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);
        stringBuilder = new StringBuilder();

        imgButton =(ImageButton)findViewById(R.id.capture_button);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textRecognizer.release();
                Toast.makeText( MainActivity.this, "Picture Taken Successfully!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if( !textRecognizer.isOperational() ){
            Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(1280, 960)
                    .setRequestedFps(2.0f)
                    .build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try{
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
        }

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if( items.size()  != 0 ){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            stringBuilder = new StringBuilder();
                            for( int i = 0; i < items.size(); i++ ){
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            textView.setText( stringBuilder.toString() );

                        }
                    });
                }
            }
        });
    }


    //TODO: Delete this method if not used later.
    //=============================================================================
    //   clickOnStopButton
    // This method stops the textRecognizer and moves the user onto the next page
    // so that the user can either decide whether to store or re-take a picture.
    //=============================================================================
    public void clickOnShutterButton(View v) {
        Button button = (Button) v;
        textRecognizer.release();
        Toast.makeText(this, "Picture Taken Successfully!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
        startActivity(intent);
        finish();
    }

    // GETTERS AND SETTERS
    public static String getText(){
        return stringBuilder.toString();
    }
}
