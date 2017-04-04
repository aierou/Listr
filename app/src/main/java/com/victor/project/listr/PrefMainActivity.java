package com.victor.project.listr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PrefMainActivity extends AppCompatActivity {

    TrackGPS gps;
    double longitude;
    double latitude;
    PrefMainActivity mContext;
    ImageButton button_send;
    ImageButton button_cancel;
    ImageButton button_gps;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    boolean EditButton;
    boolean AddButton;
    boolean DeleteButton;
    boolean PrivateButton;
    boolean PublicButton;
    boolean YesButton;
    boolean NoButton;
    String listId;
    String listName;

    @Override protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        listId = i.getStringExtra("id");
        listName = i.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        mContext = PrefMainActivity.this;

        button_send = (ImageButton) findViewById(R.id.AcceptButton);
        button_cancel = (ImageButton) findViewById(R.id.CancelButton);
        button_gps = (ImageButton) findViewById(R.id.getLocation);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        EditButton = getSharedPreferences("PREFS",0).edit().putBoolean("EditButton",false).commit();
        AddButton = getSharedPreferences("PREFS",0).edit().putBoolean("AddButton",false).commit();
        DeleteButton = getSharedPreferences("PREFS",0).edit().putBoolean("DeleteButton",false).commit();
        PrivateButton = getSharedPreferences("PREFS",0).edit().putBoolean("PrivateButton",false).commit();
        PublicButton = getSharedPreferences("PREFS",0).edit().putBoolean("PublicButton",false).commit();
        YesButton = getSharedPreferences("PREFS",0).edit().putBoolean("YesButton",false).commit();
        NoButton = getSharedPreferences("PREFS",0).edit().putBoolean("NoButton",false).commit();

        databaseReference.child("Lists").child(listId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean is_public = (boolean) dataSnapshot.child("is_public").getValue();
                if(is_public){
                    RadioButton r = (RadioButton) findViewById(R.id.PublicButton);
                    r.setChecked(true);
                }else{
                    RadioButton r = (RadioButton) findViewById(R.id.PrivateButton);
                    r.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                process_message();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new TrackGPS(mContext);

                if(gps.canGetLocation()){

                    longitude = gps.getLongitude();
                    latitude = gps.getLatitude();

                    Toast.makeText(mContext,"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    public void onUserRadioButtonClicked(View view){
        //Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        //check which button was clicked
        switch (view.getId()){
            case R.id.EditButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("EditButton",true).apply();

                break;
            case R.id.AddButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("AddButton",true).apply();

                break;
            case R.id.DeleteButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("DeleteButton",true).apply();

                break;
        }
    }

    public void onVisibilityRadioButtonClicked(View view){
        //Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        //check which button was clicked
        switch (view.getId()){
            case R.id.PrivateButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("PrivateButton",true).apply();
                getSharedPreferences("PREFS", 0).edit().putBoolean("PublicButton",false).apply();

                break;
            case R.id.PublicButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("PublicButton",true).apply();
                getSharedPreferences("PREFS", 0).edit().putBoolean("PrivateButton",false).apply();

                break;
        }
    }

    public void onAllowJoinReqButtonClicked(View view){
        //Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        //check which button was clicked
        switch (view.getId()){
            case R.id.YesButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("YesButton",true).apply();
                getSharedPreferences("PREFS", 0).edit().putBoolean("NoButton",false).apply();

                break;
            case R.id.NoButton:
                if(checked)
                    //do some function

                    getSharedPreferences("PREFS", 0).edit().putBoolean("NoButton",true).apply();
                getSharedPreferences("PREFS", 0).edit().putBoolean("YesButton",false).apply();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gps != null) gps.stopUsingGPS();
    }

    private void process_message() {

        EditButton = getSharedPreferences("PREFS", 0).getBoolean("EditButton",EditButton);
        AddButton = getSharedPreferences("PREFS", 0).getBoolean("AddButton",AddButton);
        DeleteButton = getSharedPreferences("PREFS", 0).getBoolean("DeleteButton",DeleteButton);
        PrivateButton = getSharedPreferences("PREFS", 0).getBoolean("PrivateButton",PrivateButton);
        PublicButton = getSharedPreferences("PREFS", 0).getBoolean("PublicButton",PublicButton);
        YesButton = getSharedPreferences("PREFS", 0).getBoolean("YesButton",YesButton);
        NoButton = getSharedPreferences("PREFS", 0).getBoolean("NoButton",NoButton);
        if(PublicButton){
            databaseReference.child("Lists").child(listId).child("is_public").setValue(true);
        }else if(PrivateButton){
            databaseReference.child("Lists").child(listId).child("is_public").setValue(false);
        }
        Intent i = new Intent(PrefMainActivity.this, ViewAList.class);
        i.putExtra("header", listId);
        i.putExtra("name", listName);
        PrefMainActivity.this.startActivity(i);
        finish();

        //sends the db to the server.
        String key = databaseReference.child("list_prefs").push().getKey();
        PrefUpdateMessage post = new PrefUpdateMessage(EditButton, AddButton, DeleteButton
                , PrivateButton, PublicButton, YesButton, NoButton, latitude, longitude);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/list_prefs/" + key, postValues);
        databaseReference.updateChildren(childUpdates);
    }
}