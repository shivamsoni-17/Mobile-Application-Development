package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dk7aditya.firebaseimagerecognitionthroughml.models.GroupList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Intent homeActivity = new Intent(SignUpActivity.this, HomeActivity.class);
            homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeActivity);
            overridePendingTransition(0,0);
        }
        TextView userEmail = findViewById(R.id.userEmail);
        TextView password = findViewById(R.id.passWord);
        TextView reEnterPassword = findViewById(R.id.reEnterPassWord);
        Button signUp = findViewById(R.id.signUp);
        TextView loginPageText = findViewById(R.id.loginPageText);
        TextView forgotPasswordActivityLink = findViewById(R.id.forgotPasswordActivityLink);
        signUp.setEnabled(false);

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && !password.getText().toString().isEmpty() && !reEnterPassword.getText().toString().isEmpty()){
                    signUp.setEnabled(true);
                }else{
                    signUp.setEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && !userEmail.getText().toString().isEmpty() && !reEnterPassword.getText().toString().isEmpty()){
                    signUp.setEnabled(true);
                }else{
                    signUp.setEnabled(false);
                }
            }
        });

        reEnterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && !password.getText().toString().isEmpty() && !userEmail.getText().toString().isEmpty()){
                    signUp.setEnabled(true);
                }else{
                    signUp.setEnabled(false);
                }
            }
        });

        String regexMail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
        String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#$%^&+=])(?=\\S+$).{8,}$";

        signUp.setOnClickListener(v -> {
            if(!userEmail.getText().toString().matches(regexMail)){
                userEmail.setError("Invalid Email");
                userEmail.requestFocus();
            }else if(!password.getText().toString().matches(regexPassword)){
                password.setError("Use stronger password");
                password.requestFocus();
            }else if(!password.getText().toString().equals(reEnterPassword.getText().toString())){
                reEnterPassword.setError("Passwords don't match!!");
                reEnterPassword.requestFocus();
            }else{
                Log.d(TAG, "onClick: The email and passwords satisfy regex.");
                mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if(task.isSuccessful()){
                                Log.d(TAG, "createUserWithEmail:success");
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser getuser = mAuth.getCurrentUser();
                                String email = getuser.getEmail();
                                String uid = getuser.getUid();
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("uid",uid);
                                hashMap.put("name", "");
                                hashMap.put("phone","");
                                hashMap.put("image", "");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference_database = database.getReference("UserInformation");
                                reference_database.child(uid).setValue(hashMap);
                                Intent homeActivity = new Intent(SignUpActivity.this, HomeActivity.class);
                                homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(homeActivity);
                                overridePendingTransition(0,0);
                            }else{
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(SignUpActivity.this, "All OK!!!", Toast.LENGTH_SHORT).show();
            }
        });

        loginPageText.setOnClickListener(v -> {
            Intent loginPage = new Intent(SignUpActivity.this, LoginActivity.class);
            loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginPage);
            overridePendingTransition(0,0);
        });

        forgotPasswordActivityLink.setOnClickListener(v ->{
            Intent forgotPasswordIntent = new Intent(SignUpActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
            overridePendingTransition(0,0);
        });

    }
}