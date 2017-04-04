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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;

public class MainListActivity extends AppCompatActivity{
    ListView list_public;
    ListView list_personal;
    ArrayList<ListEntity> publicLists;
    ArrayList<ListEntity> myLists;
    Location mLastLocation;
    SharedPreferences.Editor sEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(Globals.gps.getLatitude() + " " + Globals.gps.getLongitude());
        //Toast.makeText(getApplicationContext(),Globals.gps.getLatitude() + " " + Globals.gps.getLongitude(), Toast.LENGTH_LONG);
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


        //ListEntity setup
        list_public = (ListView) findViewById(R.id.list_public);
        list_personal = (ListView) findViewById(R.id.list_personal);
        publicLists = new ArrayList<>();
        myLists = new ArrayList<>();
        final CustomAdapter adapterPublic = new CustomAdapter(publicLists, this);
        final CustomAdapter adapterPersonal = new CustomAdapter(myLists, this);
        list_public.setAdapter(adapterPublic);
        list_personal.setAdapter(adapterPersonal);




        //Public list item selection
        list_public.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                ListEntity item = (ListEntity) adapter.getItemAtPosition(position);
                Intent viewlist = new Intent(getApplicationContext(), view_otherlists.class);
                viewlist.putExtra("header", item.getId());
                viewlist.putExtra("name", item.getName());
                startActivity(viewlist);
            }
        });

        //Personal list item selection
        list_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                ListEntity item = (ListEntity) adapter.getItemAtPosition(position);
                Intent viewlist = new Intent(getApplicationContext(), ViewAList.class);
                viewlist.putExtra("header", item.getId());
                viewlist.putExtra("name", item.getName());
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

        //Database stuff
        SharedPreferences preferences = getSharedPreferences("share", 0);
        sEditor = preferences.edit();
        Globals.database = FirebaseDatabase.getInstance().getReference();

        //Get private lists
        ChildEventListener userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ValueEventListener listListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) return;
                        List list = dataSnapshot.getValue(List.class);
                        myLists.add(new ListEntity(dataSnapshot.getKey(), list.name, true));
                        adapterPersonal.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                Globals.database.child("Lists").child(dataSnapshot.getValue(String.class)).addListenerForSingleValueEvent(listListener);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ChildEventListener publicListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List list = dataSnapshot.getValue(List.class);
                double lat = Globals.gps.getLatitude();
                double lng = Globals.gps.getLongitude();
                if(list.latitude-1<lat && list.latitude+1>lat
                        && list.longitude-1<lng && list.longitude+1>lng
                        && list.is_public){
                    publicLists.add(new ListEntity(dataSnapshot.getKey(), list.name, false));
                    adapterPublic.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //My lists database hook
        Globals.database.child("Users").child(Globals.username).child("lists").addChildEventListener(userListener);

        //Public lists database hook
        Globals.database.child("Lists").addChildEventListener(publicListener);
    }

    @Override
    public void onResume(){
        super.onResume();
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
            case R.id.menu_account:
                Intent i = new Intent(getApplicationContext(),ManageAccounts.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
