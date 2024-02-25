package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DisplayPersonImage extends AppCompatActivity {
    private static final String TAG = "DisplayPersonImage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_person_image);
        ImageView image = findViewById(R.id.imageFaceRecognised);
        String temperatureSet = getIntent().getStringExtra("TEMPERATURE");
        String imageURL = getIntent().getStringExtra("IMAGEURL");
       // Toast.makeText(this, imageURL,Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(image);

        //Log.d("onCreate: ", temperatureSet);
        //Toast.makeText(DisplayPersonImage.this,temperatureSet,Toast.LENGTH_SHORT).show();
        TextView setTemperature = findViewById(R.id.setTemperature);
        setTemperature.setText("TEMPERATURE: " + temperatureSet + "°F");
    }
}