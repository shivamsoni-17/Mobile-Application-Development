package shiva.cse.sahayta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.Manifest;
import android.location.Location;
import android.net.Uri;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class dashboard extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Button comp, findHospitalBtn, emg_btn;
    private TextView userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        comp = findViewById(R.id.btn_file_complaint);
        findHospitalBtn = findViewById(R.id.btn_find_hospital);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        emg_btn = findViewById(R.id.btn_1);
        userId = findViewById(R.id.userid);

        String email = getIntent().getStringExtra("email");
        if (email != null) {
            userId.setText(email); // Set the email to the TextView
        }


        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoComplaint();
            }
        });
        findHospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    findHospitals();
                }
            }
        });
        emg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEMG();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findHospitals();
            }
        }
    }
    void findHospitals() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("Location Success", "Location: " + location.getLatitude() + ", " + location.getLongitude());
                            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=hospitals+near+" + location.getLatitude() + "," + location.getLongitude());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                            else
                            {
                                Uri map = Uri.parse("https://www.google.com/maps/search/?api=1&query=hospital+near+" + location.getLatitude() + "," + location.getLongitude());
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,map);
                                startActivity(browserIntent);
                            }
                        } else {
                            Log.d("Location Error", "Location is null");
                        }
                    }
                });


    }
    void gotoComplaint() {
        Intent i = new Intent(this,complaint.class);

        startActivity(i);
    }
    void gotoEMG() {
        Intent i = new Intent(dashboard.this,emg.class);
        startActivity(i);
    }
}