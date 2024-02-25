package shiva.cse.sahayta;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment  {
    private FirebaseAuth auth;
    public EditText uemail,upass;
    private Button login;
    private String validatedEmail;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        uemail = v.findViewById(R.id.et_email);
        upass = v.findViewById(R.id.et_password);
        login = v.findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uemail.getText().toString();
                String pass = upass.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    if(!pass.isEmpty())
                    {
                        Validate(email,pass);
                        uemail = v.findViewById(R.id.et_email);
                        upass = v.findViewById(R.id.et_password);
                    }
                    else
                    {
                        upass.setError("Password cannot be empty!");
                    }
                }
                else if(email.isEmpty())
                {
                    uemail.setError("Email cannot be empty!");
                }
                else
                {
                    uemail.setError("Enter Valid email/password!!");
                }
            }
        });

        return v;
    }

    public void prefillEmail(String email) {
        if (uemail != null) {
            uemail.setText(email);
        }
    }
    private void Validate(String email,String pass) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(),"Login Successfull",Toast.LENGTH_SHORT).show();
                        validatedEmail = email;
                        gotoDashboard();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uemail.setText("");
                        upass.setText("");
                        Toast.makeText(getActivity(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void gotoDashboard() {
        Intent i = new Intent(getActivity(), dashboard.class);
        i.putExtra("email", validatedEmail);
        startActivity(i);
    }

}