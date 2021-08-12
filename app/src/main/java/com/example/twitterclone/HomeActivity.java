package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    List<Tweet> tweets = new ArrayList<>();
    FloatingActionButton addTweetBtn;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tweetRef = database.getReference("tweets");

        TweetAdapter tweetAdapter = new TweetAdapter(HomeActivity.this, tweets);
        RecyclerView tweetRV = findViewById(R.id.tweetRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        tweetRV.setLayoutManager(linearLayoutManager);
        tweetRV.setAdapter(tweetAdapter);


        //Nav
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.item10) {
                    //sign out using firebase
                    FirebaseAuth.getInstance().signOut();

                    //clear prefs
                    PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putBoolean("isLogged", true).clear().commit();
                    PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putString("displayName", "").clear().commit();
                    PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putString("username", "").clear().commit();

                    //finish to avoid backs
                    finish();
                    //move to main activity
                    Intent intent = new Intent(HomeActivity.this, LauncherActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        ImageButton btnMenu = findViewById(R.id.burger_menu);
        drawer = findViewById(R.id.drawer_layout);

        View headerLayout = navigationView.getHeaderView(0);
        TextView usernameHeader = headerLayout.findViewById(R.id.usernameHeader);
        TextView displayNameHeader = headerLayout.findViewById(R.id.displayNameHeader);

        usernameHeader.setText("@" + PreferenceManager.getDefaultSharedPreferences(this).getString("displayName", ""));
        String originalDisplayName = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        displayNameHeader.setText(originalDisplayName.substring(0, 1).toUpperCase() + originalDisplayName.substring(1));

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {         //gravity start means start the animation from the lft side>> open drawer from start
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        tweetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    tweets.clear();
                    //tweetAdapter.clear();
                    for (DataSnapshot tweetSnapshot : snapshot.getChildren()) {
                        tweets.add(new Tweet(tweetSnapshot.child("username").getValue().toString(), tweetSnapshot.child("name").getValue().toString(), tweetSnapshot.child("tweet").getValue().toString(), tweetSnapshot.child("time_published").getValue().toString()));
                        tweetAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        addTweetBtn = findViewById(R.id.addTweetBtn);
        addTweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewTweetActivity.class);
                startActivity(intent);
            }
        });

    }


}