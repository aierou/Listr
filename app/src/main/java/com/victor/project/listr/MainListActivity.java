package com.victor.project.listr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity {
    SharedPreferences.Editor sEditor;
    DatabaseReference database1;
    String username;
    int i;
    LinearLayout ll;
    LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

         ll = (LinearLayout) findViewById(R.id.linear1);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //buttons[] = ()
        SharedPreferences preferences = getSharedPreferences("share",0);
        sEditor = preferences.edit();
        final Intent intent = getIntent();
        username = intent.getStringExtra("username").concat("~headers");
        database1  = FirebaseDatabase.getInstance().getReference();
        i=0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newl = new Intent(MainListActivity.this, NewList.class);
                newl.putExtra("username",intent.getStringExtra("username"));
                startActivity(newl);

            }
        });

        ChildEventListener imageListener = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String header = dataSnapshot.getKey();

                final Button button= new Button(getApplicationContext());
                button.setText(header);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setId(i);
                button.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                    String head = button.getText().toString();
                        Intent viewlist = new Intent(MainListActivity.this,ViewAList.class);
                        viewlist.putExtra("header",head);
                        viewlist.putExtra("username",intent.getStringExtra("username"));
                        startActivity(viewlist);


                    }
                });
                ll.addView(button);
                i++;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };

        database1.child(username).addChildEventListener(imageListener);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_logout:
                sEditor.putBoolean("stayloggedin", false);
                sEditor.commit();
                Intent outIntent = new Intent(MainListActivity.this,Login.class);
                startActivity(outIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
