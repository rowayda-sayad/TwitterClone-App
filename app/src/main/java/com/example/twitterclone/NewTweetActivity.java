package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewTweetActivity extends AppCompatActivity {
    Button tweet;
    ImageButton backTweetBtn;
    EditText tweetEdt;
    String name, uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);

        name = PreferenceManager.getDefaultSharedPreferences(this).getString("displayName", "");
        uname = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tweetRef = database.getReference("tweets");

        backTweetBtn = findViewById(R.id.backTweetBtn);
        tweet = findViewById(R.id.tweetBtn);

        backTweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tweetEdt = findViewById(R.id.tweetContentText);

        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweetEdt.getText().toString().equals("")) {
                    tweet.setEnabled(false);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma mm/dd/yy", Locale.US);
                    String date = simpleDateFormat.format(new Date());

                    String key = tweetRef.push().getKey();
                    tweetRef.child(key).child("username").setValue(uname);
                    tweetRef.child(key).child("name").setValue(name);
                    tweetRef.child(key).child("tweet").setValue(tweetEdt.getText().toString());
                    tweetRef.child(key).child("time_published").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            onBackPressed();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                                                 @Override
                                                 public void onCanceled() {
                                                     Toast.makeText(NewTweetActivity.this, "Something went wrong while saving your tweet", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                    );
                } else {
                    Toast.makeText(NewTweetActivity.this, "Write tweet first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}