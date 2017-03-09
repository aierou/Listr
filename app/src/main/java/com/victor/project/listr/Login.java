package com.victor.project.listr;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Map;
import java.util.Set;


public class Login extends AppCompatActivity {

    SharedPreferences.Editor sEditor;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("share",0);
        sEditor = preferences.edit();
        boolean skip = preferences.getBoolean("stayloggedin",false);
        if(skip){
            String skipName = preferences.getString("username", "");
            Intent skipinfo = new Intent(Login.this, MainListActivity.class);
            skipinfo.putExtra("username", skipName);
            Login.this.startActivity(skipinfo);
            finish();

        }

        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();



        setContentView(R.layout.login);
        final CheckBox savelogin = (CheckBox) findViewById(R.id.savecheckbox);

        Button login = (Button) findViewById(R.id.login);
        Button newguy = (Button) findViewById(R.id.createnew);
        final EditText name = (EditText) findViewById(R.id.nametext);
        final EditText password = (EditText) findViewById(R.id.passtext);
        final TextView badlogin = (TextView) findViewById(R.id.errorlogin);

        final ValueEventListener usernameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.exists())){
                    Toast.makeText(getBaseContext(),"That username does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = dataSnapshot.getKey();
                String pass = dataSnapshot.getValue().toString();
                if(!(password.getText().toString().equals(pass))){
                    Toast.makeText(getApplicationContext(),"Incorrect password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(savelogin.isChecked()){
                    sEditor.putBoolean("stayloggedin", true);
                    sEditor.putString("password", pass);
                    sEditor.commit();
                }
                sEditor.putString("username",name);
                sEditor.commit();
                Intent userinfo = new Intent(Login.this, MainListActivity.class);
                userinfo.putExtra("username", name);
                Login.this.startActivity(userinfo);
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                badlogin.setText("Username Unrecognized");
            }
        };

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                mdatabase.child("~AllUsers").child(name.getText().toString()).addListenerForSingleValueEvent(usernameListener);
                // Intent connected = new Intent(MainActivity.this, CameraUIActivity.class);
                // MainActivity.this.startActivity(connected);

            }
        });
        newguy.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent newuser = new Intent(Login.this, NewUser.class);
                startActivity(newuser);


            }
        });
    }


}
