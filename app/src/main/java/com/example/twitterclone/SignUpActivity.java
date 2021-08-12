package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ImageButton back;
    Button finish;

    EditText username;
    EditText pass;

    String name, email, dob;

    private FirebaseAuth mAuth;
    final String TAG = getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        dob = getIntent().getStringExtra("dob");


        back = findViewById(R.id.back);
        finish = findViewById(R.id.finish);

        username = findViewById(R.id.usenameEdt);
        pass = findViewById(R.id.passEdt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(username.getText().toString(), pass.getText().toString())) {
                    register(email, pass.getText().toString());
                } else {
                    Toast.makeText(SignUpActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Boolean validate(String username, String pass) {
        if (username.equals("") || pass.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void nextPage(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        userRef.child(user.getUid()).child("name").setValue(name);
        userRef.child(user.getUid()).child("email").setValue(email);
        userRef.child(user.getUid()).child("dob").setValue(dob);
        userRef.child(user.getUid()).child("username").setValue(username.getText().toString());

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isLogged", true).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("displayName", name).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("username", username.getText().toString()).commit();

        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_LONG).show();
                            nextPage(user);
                        } else {

                            //check if user already exists
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(SignUpActivity.this, "You are already registered to Twitter Clone." + task.getException(),
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();

                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }


}