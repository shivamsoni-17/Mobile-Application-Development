package shiva.cse.sahayta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class complaint extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 0;
    String phone ;
    EditText otpinput,phn,name,desc;
    Button btn,sub ;
    Spinner type;
    String verificationCode;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        type = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.complaint_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        phn = findViewById(R.id.editText4);
        otpinput = findViewById(R.id.editText5);
        btn = findViewById(R.id.button);
        sub = findViewById(R.id.button2);
        name = findViewById(R.id.editTextText);
        desc = findViewById(R.id.editTextText3);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = "+91"+phn.getText().toString();
                sendotp(phone);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpinput.getText().toString();
                verifyVerificationCode(otp);
            }
        });

    }

    private BroadcastReceiver otpReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Initialize the BroadcastReceiver
        otpReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SmsBroadcastReceiver.OTP_RECEIVED_ACTION.equals(intent.getAction())) {
                    String otp = intent.getStringExtra("otp");
                    if (otp != null) {
                        otpinput.setText(otp);
                    }
                }
            }
        };
        // Register the receiver
        registerReceiver(otpReceiver, new IntentFilter(SmsBroadcastReceiver.OTP_RECEIVED_ACTION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the receiver
        if (otpReceiver != null) {
            unregisterReceiver(otpReceiver);
            otpReceiver = null;
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void sendotp(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e)
                    {
                        Toast.makeText(complaint.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        verificationCode = verificationId;
                        Toast.makeText(getApplicationContext(), "OTP sent successfully.", Toast.LENGTH_SHORT).show();
                    }
                })          // OnVerificationStateChangedCallbacks
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options); // This line starts the verification process
    }
    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                    {
                        String p,n,t,d;
                        p = phn.getText().toString();
                        n=name.getText().toString();
                        Object selectedItem = type.getSelectedItem();
                        t = selectedItem != null ? selectedItem.toString() : "";
                        d= desc.getText().toString();

                        if(p.isEmpty())
                        {
                            phn.setError("Can't be empty.");
                        }
                        if(n.isEmpty())
                        {
                            name.setError("Can't be empty.");
                        }
                        if(d.isEmpty()){
                            desc.setError("Can't be empty.");
                        }
                        addtoDB(p,n,t,d);

                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(complaint.this, "Verification Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void addtoDB(String p, String n, String t, String d) {
        HashMap<String , Object> data = new HashMap<>();
        data.put("Phone",p);
        data.put("Name",n);
        data.put("Type",t);
        data.put("Desc",d);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Complaints");

        String key = myRef.push().getKey();
        data.put("key",key);
        myRef.child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(complaint.this, "Complaint Registered Successfully", Toast.LENGTH_LONG).show();
                phn.getText().clear();
                name.getText().clear();
                desc.getText().clear();

                Intent downloadIntent = new Intent(complaint.this, DownloadService.class);
                startService(downloadIntent);
            }
        });
    }


}
