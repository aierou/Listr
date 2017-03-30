package com.victor.project.listr;

/**
 * Created by Beta on 3/6/2017.
 */

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewUser extends AppCompatActivity {
    static String name;
    static String pass1;
    static String pass2;
    final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);


        final ValueEventListener usernameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean there = dataSnapshot.exists();
                if(there){
                    Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_SHORT).show();

                    return;
                }
                else {doTheThings();};

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        setContentView(R.layout.activity_new_user);

        Button makeuser = (Button) findViewById(R.id.createUser);
        final EditText username = (EditText) findViewById(R.id.newname);
        final EditText passText1 = (EditText) findViewById(R.id.newpass1);
        final EditText passText2 = (EditText) findViewById(R.id.newpass2);
        final TextView error = (TextView) findViewById(R.id.errormessage);


        makeuser.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                name = username.getText().toString();
                pass1 = passText1.getText().toString();
                pass2 = passText2.getText().toString();

                if (!(pass1.equals(pass2))) {
                    error.setText("Passwords Do Not Match");
                    return;
                }

                mdatabase.child("Users").child(name).addListenerForSingleValueEvent(usernameListener);


            }
        });


    }

    public void doTheThings(){
        User user = new User(pass1);
        mdatabase.child("Users").child(name).setValue(user);

        Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_SHORT).show();
        finish();
    }


}
