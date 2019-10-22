package com.renny.user.mtaxi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mLogin,mRegisteration;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth=FirebaseAuth.getInstance();

        fireAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent=new Intent(DriverLoginActivity.this,DriverMapsActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };



        mEmail=(EditText)findViewById(R.id.email);
        mPassword=(EditText)findViewById(R.id.password);
        mLogin=(Button)findViewById(R.id.login);
        mRegisteration=(Button)findViewById(R.id.registration);

        mRegisteration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Registration unsuccessful!",Toast.LENGTH_LONG).show();
                        }else{
                            String user_id=mAuth.getCurrentUser().getUid();
                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference current_user_db= firebaseDatabase.getReference().child("Users").child("Drivers").child(user_id);
                            current_user_db.setValue(true);
                            Toast.makeText(getApplicationContext(),"Registration done!",Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Login unsuccessful!",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Login Passed!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireAuthListener);
    }
}
