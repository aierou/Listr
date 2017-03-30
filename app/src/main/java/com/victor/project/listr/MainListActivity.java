package com.victor.project.listr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ListView list_public;
    ListView list_personal;
    ArrayList<List> publicLists;
    ArrayList<List> myLists;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    SharedPreferences.Editor sEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        //Setting up tabs
        TabHost tabs = (TabHost) findViewById(R.id.tabs);
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("public")
                .setIndicator("Public Lists")
                .setContent(R.id.tab1));

        tabs.addTab(tabs.newTabSpec("personal")
                .setIndicator("My Lists")
                .setContent(R.id.tab2));

        tabs.setCurrentTabByTag("personal");


        //List setup
        list_public = (ListView) findViewById(R.id.list_public);
        list_personal = (ListView) findViewById(R.id.list_personal);
        publicLists = new ArrayList<>();
        myLists = new ArrayList<>();
        CustomAdapter adapterPublic = new CustomAdapter(publicLists, this);
        final CustomAdapter adapterPersonal = new CustomAdapter(myLists, this);
        list_public.setAdapter(adapterPublic);
        list_personal.setAdapter(adapterPersonal);




        //Public list item selection
        list_public.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                List item = (List) adapter.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Personal list item selection
        list_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                List item = (List) adapter.getItemAtPosition(position);
                Intent viewlist = new Intent(getApplicationContext(), ViewAList.class);
                viewlist.putExtra("header", item.getId());
                viewlist.putExtra("username", Globals.username);
                startActivity(viewlist);
            }
        });

        //Register new list button
        findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newList();
            }
        });

        //Location services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        //Database stuff
        SharedPreferences preferences = getSharedPreferences("share", 0);
        sEditor = preferences.edit();
        final Intent intent = getIntent();
        Globals.username = intent.getStringExtra("username");
        Globals.database = FirebaseDatabase.getInstance().getReference();

        ChildEventListener imageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String header = dataSnapshot.getKey();
                myLists.add(new List(header, header, true));
                adapterPersonal.notifyDataSetChanged();
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

        Globals.database.child(Globals.username.concat("~headers")).addChildEventListener(imageListener);
    }

    public void newList(){
        Intent newl = new Intent(getApplicationContext(), NewList.class);
        newl.putExtra("username", Globals.username);
        startActivity(newl);
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
                Intent outIntent = new Intent(getApplicationContext(),Login.class);
                startActivity(outIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Location services methods
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double lat = mLastLocation.getLatitude();
                double lng = mLastLocation.getLongitude();
                //Make request to database for public lists and populate
            }
        }catch(SecurityException e){
            //Request permissions/fail silently
        }
    }

    @Override
    public void onConnectionSuspended(int cause){
        //Don't care
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        //Don't care
    }
}
