package shiva.cse.sahayta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class emg extends AppCompatActivity {

    // Define the emergency options and corresponding phone numbers
    private final String[] emergencyOptions = {
            "Disaster Report", "Police Report", "Fire Report", "Child Helpline", "Women Helpline", "Ambulance Services"
    };
    private final String[] phoneNumbers = {"9711077372", "100", "101", "1098", "181", "102"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emg);

        ListView listOptions = findViewById(R.id.list_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emergencyOptions);
        listOptions.setAdapter(adapter);

        listOptions.setOnItemClickListener((parent, view, position, id) -> {
            if (position < phoneNumbers.length) {
                String phoneNumber = phoneNumbers[position];
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }
}