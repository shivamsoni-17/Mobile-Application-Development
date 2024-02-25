
package shiva.cse.sahayta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText rname,remail,rpass1,rpass2;
    private Button reg;



    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);


        rname = v.findViewById(R.id.et_name);
        remail = v.findViewById(R.id.et_email);
        rpass1 = v.findViewById(R.id.et_password);
        rpass2 = v.findViewById(R.id.et_repassword);
        reg = v.findViewById(R.id.btn_register);


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,email,pass1,pass2;
                name = rname.getText().toString().trim();
                email = remail.getText().toString().trim();
                pass1 = rpass1.getText().toString().trim();
                pass2 = rpass2.getText().toString().trim();

                if(name.isEmpty()) {
                    rname.setError("Enter the name!!");
                }
                if(email.isEmpty()) {
                    remail.setError("Email cannot be empty!");
                }
                if(pass1.isEmpty()) {
                    rpass1.setError("Password cannot be empty!");
                }
                if(pass2.isEmpty()) {
                    rpass2.setError("Re-enter the password!!");
                }
                else
                {
                    if(pass1.equals(pass2))
                    {
                        RegisterUser(name,email,pass1);
                        rname.setText("");
                        remail.setText("");
                        rpass1.setText("");
                        rpass2.setText("");
                    }
                    else
                    {
                        rpass2.setError("Password did not match");
                    }
                }
            }
        });
        return v;
    }

    private void RegisterUser(String name,String email,String pass)
    {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(),"User Registered",Toast.LENGTH_SHORT).show();
                            if (getActivity() instanceof FragmentCommunicationInterface) {
                                ((FragmentCommunicationInterface) getActivity()).onEmailRegistered(email);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(getActivity(), "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }



}